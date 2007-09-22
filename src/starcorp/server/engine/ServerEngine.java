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

import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.engine.ServerEngine
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public class ServerEngine {
	private static final int THREAD_POOL = 10;
	
	private int taskNumber = 1;
	private ExecutorService execService = Executors.newFixedThreadPool(THREAD_POOL);
	private final IEntityStore entityStore;
	private Vector<AServerTask> tasks = new Vector<AServerTask>();

	public ServerEngine(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	public void schedule(AServerTask task) {
		task.setEngine(this);
		task.setTaskNumber(taskNumber);
		task.setEntityStore(entityStore);
		tasks.add(task);
		execService.submit(task);
		taskNumber++;
	}
	
	public void shutdown() {
		execService.shutdownNow();
		entityStore.shutdown();
		System.exit(0);
	}
	
	void done(AServerTask task) {
		tasks.remove(task);
	}
	
	public Enumeration<AServerTask> runningTasks() {
		return tasks.elements();
	}
	
}
