/**
 *  Copyright 2007 Seyed Razavi
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and limitations under the License. 
 */
package starcorp.server.engine;

import org.apache.commons.logging.Log;

import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.engine.AServerTask
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public abstract class AServerTask implements Runnable {
	public static final String getDisplayDuration(long milliseconds) {
		int seconds = (int) milliseconds / 1000;
		if(seconds < 60) { // less than a minute
			return seconds + "s";
		}
		else if(seconds < 3600){ // less than an hour
			int minutes = (int) (seconds / 60);
			int secs = seconds % (minutes * 60);
			return minutes +"m "+secs+"s";
		}
		else {
			int hours = (int) (seconds / 3600);
			int secs = seconds % (hours * 3600);
			int minutes = (int) (secs / 60);
			secs = seconds % (minutes * 60);
			return hours + "h "+minutes +"m "+secs+"s";
		}
	}

	protected ServerEngine engine;
	protected IEntityStore entityStore;
	protected int taskNumber;
	private boolean done = false;
	
	public AServerTask() {
	}
	
	public void setEngine(ServerEngine engine) {
		this.engine = engine;
	}
	
	public void setEntityStore(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	public void setTaskNumber(int number) {
		this.taskNumber = number;
	}
	
	protected void beginTransaction() {
		entityStore.beginTransaction();
	}
	
	protected void rollback() {
		entityStore.rollback();
	}
	
	protected void commit() {
		entityStore.commit();
	}

	public void run() {
		try {
			long start = System.currentTimeMillis();
			if(getLog().isDebugEnabled())
				getLog().debug("[Task: " + taskNumber +"] started.");
			doJob();
			
			long time = System.currentTimeMillis() - start;
			if(getLog().isDebugEnabled())
				getLog().debug("[Task: " + taskNumber +"] done in " + getDisplayDuration(time) + ".");
		}
		catch(Exception e) {
			getLog().error("[Task: " + taskNumber +"] "+ e.getMessage(),e);
			rollback();
			onException(e);
		}
		engine.done(this);
		done = true;
	}
	
	protected void onException(Exception e) {
		// do nothing
	}
	
	public boolean isDone() {
		return this.done;
	}
	
	public boolean isHighPriority() {
		return false;
	}
	
	protected abstract void doJob() throws Exception;
	protected abstract Log getLog();

	protected String getName() {
		String name = getClass().getSimpleName();
		if(name == null || name.length() == 0) {
			name = getClass().getName();
			int end = name.lastIndexOf("$");
			if(end == -1) end = name.length() - 1;
			name = name.substring(name.lastIndexOf(".") + 1, end);
		}
		return name;
	}
	
	@Override
	public String toString() {
		return "[" + taskNumber + "] " + getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AServerTask other = (AServerTask) obj;
		if (taskNumber != other.taskNumber)
			return false;
		return true;
	}
}
