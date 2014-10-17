package aoesoft.pingyin;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class JFace {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		//---
		shell.setLayout(new FillLayout());
		ListViewer lv = new ListViewer(shell);
		lv.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				AssetGroup g = (AssetGroup)element;
				return "<"+g.id+">"+g.name;
			}
		});
		lv.setContentProvider(new ArrayContentProvider());
		lv.setInput(AssetGroup.getData());
		
		
		
		TableViewer tv = new TableViewer(shell, SWT.FULL_SELECTION|SWT.SINGLE|SWT.BORDER);
		Table table = tv.getTable();
		table.setHeaderVisible(true);
		
		String[] columns = new String[]{"ID", "Name"};
		for(int i=0; i<columns.length; i++){
			TableColumn c = new TableColumn(table, SWT.CENTER);
			c.setText(columns[i]);
			c.pack();
		}
		
		tv.setLabelProvider(new MyTableLabelProvider());
		tv.setContentProvider(new ArrayContentProvider());
		tv.setInput(AssetGroup.getData());
		
		
		
		TreeViewer tree = new TreeViewer(shell);
		tree.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});
		tree.setContentProvider(new MyTreeContentProvider());
		
		AssetGroup[] data = AssetGroup.getData();
		AssetGroup.convert(data);  //做数据转换，计算出其父子节点
		tree.setInput(new AssetGroup[]{data[0]}); //只能传递一个root节点，否则会同时展现全部节点，并且需要包装为数组类型
		tree.expandAll();
		
		
		//---
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
		shell.dispose();
	}
	
	private static class MyTreeContentProvider extends ArrayContentProvider implements ITreeContentProvider{
		@Override
		public Object[] getChildren(Object element) {
			AssetGroup target = (AssetGroup)element;
			return target.children;
		}

		@Override
		public Object getParent(Object element) {
			AssetGroup target = (AssetGroup)element;
			return target.parent;
		}

		@Override
		public boolean hasChildren(Object element) {
			AssetGroup target = (AssetGroup)element;
			return target.children != null && target.children.length>0;
		}
	}
	
	private static class MyTableLabelProvider extends LabelProvider implements ITableLabelProvider{

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int index) {
			AssetGroup g = (AssetGroup)element;
			switch (index) {
			case 0:
				return g.id;
			case 1:
				return g.name;
			default:
				break;
			}
			return null;
		}}
	
	private static class AssetGroup{
		private String id;
		private String name;
		private String fid;
		private AssetGroup[] children;
		private AssetGroup parent;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFid() {
			return fid;
		}
		public void setFid(String fid) {
			this.fid = fid;
		}
		
		public AssetGroup(String id, String name){
			this(id, name, null);
		}
		
		public AssetGroup[] getChildren(){
			return this.children;
		}
		
		public AssetGroup getParent(){
			return this.parent;
		}
		
		public AssetGroup(String id, String name, String fid){
			this.id = id;
			this.name = name;
			this.fid = fid;
		}
		
		public String toString(){
			return this.name;
		}
		
		public static AssetGroup[] getData(){
			return new AssetGroup[]{
					new AssetGroup("0", "Root","-1"),
					new AssetGroup("1", "江苏", "0"),
					new AssetGroup("2", "浙江", "0"),
					new AssetGroup("3", "广东", "0"),
					new AssetGroup("11", "南京", "1"),
					new AssetGroup("12", "镇江", "1"),
					new AssetGroup("13", "南通", "1"),
					new AssetGroup("14", "淮安", "1"),
					new AssetGroup("15", "连云港", "1"),
			};
		}
		
		public static void convert(AssetGroup[] data){
			for(AssetGroup g : data){
				List<AssetGroup> children = new LinkedList<AssetGroup>();
				for(AssetGroup c : data){
					if(g.id.equals(c.id))
						continue;
					
					if(g.id.equals(c.fid)){
						children.add(c);
						c.parent = g;
					}
				}
				g.children = children.toArray(new AssetGroup[0]);
			}
		}
	}

}
