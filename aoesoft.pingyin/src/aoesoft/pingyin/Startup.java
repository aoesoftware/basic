package aoesoft.pingyin;

import org.eclipse.ui.IStartup;

/**
 * 程序启动时候就启动本类，必须设置扩展点
 * @author tangxiucai
 *
 */
public class Startup implements IStartup{

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		System.err.println("!!! executing....!!!");
		
		//Platform.getLog(Activator.getContext().getBundle()).log(new Status(IStatus.INFO, this.getClass().getName(), "..!! start"));
		
	}

}
