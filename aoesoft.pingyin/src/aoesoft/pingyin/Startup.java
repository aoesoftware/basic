package aoesoft.pingyin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;

/**
 * ��������ʱ����������࣬����������չ��
 * @author tangxiucai
 *
 */
public class Startup implements IStartup{

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		System.err.println("!!! executing....!!!");
		
		Platform.getLog(Activator.getContext().getBundle()).log(new Status(IStatus.INFO, this.getClass().getName(), "..!! start"));
		
	}

}