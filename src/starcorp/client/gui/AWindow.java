package starcorp.client.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class AWindow implements IComponent {

	protected final Display display;
	protected final Shell shell;

	public AWindow(Display display) {
		this(display, SWT.CLOSE);
	}
	
	public AWindow(Display display, int style) {
		this.display = display;
		this.shell = new Shell(display,style);
		this.shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		    	  close();
		      }
		});
	}
	
	protected void center() {
		Rectangle bounds = display.getBounds();
		Point size = shell.getSize();
		
		int x = (bounds.width - size.x) / 2;
		int y = (bounds.height - size.y) / 2;
		
		shell.setBounds(x, y, size.x, size.y);
	}

	public void focus() {
		shell.forceFocus();
	}
	
	public void pack() {
		shell.pack();
	}

	public int messageBox(String heading, String text, int style) {
		MessageBox msgBox = new MessageBox(shell, style);
		msgBox.setText(heading);
		msgBox.setMessage(text);
		return msgBox.open();
	}
	
	public Point computeSize() {
		return shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	protected void close() {
		dispose();
	}
}