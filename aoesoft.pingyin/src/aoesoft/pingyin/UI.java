package aoesoft.pingyin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class UI {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		//---
		shell.setLayout(new FillLayout());
		ListViewer lv = new ListViewer(shell);
		lv.setContentProvider(new ArrayContentProvider());
		ILabelDecorator decorator = new ILabelDecorator() {
			@Override
			public void removeListener(ILabelProviderListener arg0) {
			}
			
			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public void addListener(ILabelProviderListener arg0) {
			}
			
			@Override
			public String decorateText(String arg0, Object arg1) {
				System.out.println(arg0+"=="+arg1);
				if(arg0.contains("ang")){
					Label l = (Label)arg1;
					l.setForeground(new Color(null, 255,0,0));
				}
				if(arg0.length() > 3)
					arg0 = arg0.substring(0,3)+"...";
				return "-->"+arg0;
			}
			
			@Override
			public Image decorateImage(Image arg0, Object arg1) {
				return null;
			}
		};
		lv.setLabelProvider(new DecoratingLabelProvider(new MyLabelProvider(), decorator));
		lv.setInput(Person.example());
		lv.setComparator(new ViewerComparator(){
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person)e1;
				Person p2 = (Person)e2;
				return -p1.firstName.compareTo(p2.firstName);
			}
		});
		
		lv.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection s = (IStructuredSelection) e.getSelection();
				System.out.println(s.size());
				for(Object obj : s.toArray())
					System.out.println(obj);
			}
		});
		
		lv.add(new Person("X", "yz", 18));
		
		
		
		
		TableViewer tv = new TableViewer(shell, SWT.SINGLE|SWT.FULL_SELECTION);
		final Table table = tv.getTable();
		table.setHeaderVisible(true);
		
		String[] columns = new String[]{"Id", "Name", "LastName", "Age"};
		int[] columnWidths = new int[]{100,200,240,100};
		int[] columnAlign = new int[]{SWT.CENTER, SWT.LEFT, SWT.LEFT, SWT.CENTER};
		
		for(int i=0; i<columns.length; i++){
			TableColumn column = new TableColumn(table, columnAlign[i]);
			column.setText(columns[i]);
			column.setWidth(columnWidths[i]);
		}
		
		tv.setLabelProvider(new MyTableLabelProvider());
		tv.setContentProvider(new ArrayContentProvider());
		tv.setInput(Person.example());
		
		
		
		final TreeViewer treev = new TreeViewer(shell);
		treev.setLabelProvider(new MyLabelProvider());
		treev.setContentProvider(new TreeContentProvider());
		treev.setInput(Person.example());
		
		treev.expandToLevel(2);
		
		
		
		final TextViewer textV = new TextViewer(shell, SWT.MULTI|SWT.V_SCROLL);
		final String txt = "This is eclipse basic book.";
		Document doc = new Document(txt);
		textV.setDocument(doc);
		
		final Text txtSearch = new Text(shell, SWT.NULL);
		Button btnSearch = new Button(shell, SWT.None);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TextPresentation style = new TextPresentation();
				textV.setDocument(new Document(textV.getTextWidget().getText()));
				textV.refresh();
				
				String searchTxt = txtSearch.getText();
				int fromIndex = 0;
				do{
					fromIndex = txt.indexOf(searchTxt, fromIndex);
					if(fromIndex == -1)
						break;
					style.addStyleRange(new StyleRange(fromIndex, searchTxt.length(), null, new Color(null, 255, 0, 0)));
					fromIndex += searchTxt.length();
				}while(true);
				textV.changeTextPresentation(style, true);
			}
		});
		//---
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
		shell.dispose();
	}
	
	private static class MyLabelProvider extends LabelProvider{
		@Override
		public Image getImage(Object element) {
			//return new Image(null, UI.class.getClassLoader().getResourceAsStream("aoesoft/pingyin/a.bmp"));
			return null;
		}

		@Override
		public String getText(Object element) {
			Person p = (Person)element;
			return p.toString();
		}
	}
	
	private static class MyTableLabelProvider extends LabelProvider implements ITableLabelProvider{

		@Override
		public Image getColumnImage(Object obj, int i) {
			return null;
			//return new Image(null, UI.class.getClassLoader().getResourceAsStream("aoesoft/pingyin/a.bmp"));
		}

		@Override
		public String getColumnText(Object obj, int i) {
			Person p = (Person)obj;
			switch (i) {
			case 0:
				return "id-"+i;
			case 1:
				return p.firstName;
			case 2:
				return p.lastName;
			case 3:
				return p.age+"";
			default:
				break;
			}
			return null;
		}
		
	}
	
	private static class TreeContentProvider extends ArrayContentProvider implements ITreeContentProvider{

		@Override
		public Object[] getChildren(Object obj) {
			Person p = (Person)obj;
			return p.children;
		}

		@Override
		public Object getParent(Object obj) {
			Person p = (Person)obj;
			return p.parent;
		}

		@Override
		public boolean hasChildren(Object obj) {
			Person p = (Person)obj;
			return p.children != null && p.children.length>0;
		}
	}
	
	public static class Person{
		private String firstName;
		private String lastName;
		private int age;
		private Person parent = null;
		private Person[] children = new Person[0];
		
		public Person(String firstName, String lastName, int age){
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
		}
		
		public Person(String firstName, String lastName, int age, Person[] children){
			this(firstName, lastName, age);
			this.children = children;
			for(Person c : children)
				c.parent = this;
		}
		
		public String toString(){
			return this.firstName + " " + this.lastName;
		}
		
		public static Person[] example(){
			return new Person[]{
					new Person("Sun","xin", 29, 
							new Person[]{new Person("Sun", "Y", 2, new Person[]{new Person("Sun", "YY", 2)})}), 
					new Person("Tang", "xc", 27)
					};
		}
	}
	
	public static void main2(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		//////////////////////////
		//shell.setBounds(25, 25, 200, 120);
		GridLayout grid = new GridLayout(2, false);
		shell.setLayout(grid);
		
		GridData gd = null;
		Label lbl1 = new Label(shell, SWT.NULL);
		lbl1.setText("Entry your information");
		gd = new GridData();
		gd.horizontalSpan = 2;
		//gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		lbl1.setLayoutData(gd);
		
		Label lblUsername = new Label(shell, SWT.NULL);
		lblUsername.setText("Name");
		
		Text txtUsername = new Text(shell, SWT.NULL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		txtUsername.setLayoutData(gd);
		
		
		Group groupSource = new Group(shell, SWT.NULL);
		groupSource.setText("工程列表");
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 500;
		groupSource.setLayout(new FillLayout());
		groupSource.setLayoutData(gd);
		
		final Table tableSource = new Table(groupSource, SWT.FULL_SELECTION);
		tableSource.setHeaderVisible(true);

		TableColumn colSeq = new TableColumn(tableSource, SWT.NULL);
		colSeq.setText("SEQ");
		colSeq.pack();
		
		TableColumn colName = new TableColumn(tableSource, SWT.NULL);
		colName.setText("Project");
		
		TableColumn colPath = new TableColumn(tableSource, SWT.NULL);
		colPath.setText("Path");
	
		
		TableItem t1= new TableItem(tableSource, SWT.NONE);
		t1.setText(new String[]{"1", "Portal",""});
		
		TableItem t2= new TableItem(tableSource, SWT.NONE);
		t2.setText(new String[]{"2", "Interface",""});

		colName.pack();
		
		
		//---------------
		Group groupTarget = new Group(shell, SWT.NULL);
		groupTarget.setLayout(new FillLayout());
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		groupTarget.setLayoutData(gd);
		groupTarget.setText("详情");
		
		final Text txtTarget = new Text(groupTarget, SWT.H_SCROLL|SWT.READ_ONLY);
		
		
		DragSource ds = new DragSource(tableSource, DND.DROP_MOVE|DND.DROP_COPY);
		ds.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		ds.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent dragsourceevent) {
				if(tableSource.getSelectionCount() == 0)
					dragsourceevent.doit = false;
			}
			
			@Override
			public void dragSetData(DragSourceEvent dragsourceevent) {
				//if(TextTransfer.getInstance().isSupportedType(dragsourceevent.dataType)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("project", tableSource.getSelection()[0].getText(1));
					map.put("path", tableSource.getSelection()[0].getText(2));
					System.out.println(map);
					dragsourceevent.data = map.toString();
				//}
			}
			
			@Override
			public void dragFinished(DragSourceEvent dragsourceevent) {
				
			}
		});
		
		
		
		DropTarget dt = new DropTarget(txtTarget, DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
		dt.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dt.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent droptargetevent) {
				
			}
			
			@Override
			public void drop(DropTargetEvent droptargetevent) {
				txtTarget.setText(droptargetevent.data.toString());
			}
			
			@Override
			public void dragOver(DropTargetEvent droptargetevent) {
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent droptargetevent) {
				
			}
			
			@Override
			public void dragLeave(DropTargetEvent droptargetevent) {
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent droptargetevent) {
				
			}
		});
		
		//////////////////////////
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		shell.dispose();
		display.dispose();
	}
	
	public static void main1(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		//---
		shell.setText("Smp");
		shell.setLayout(new FillLayout());
		
		/*Label lbl = new Label(shell, SWT.SCROLL_LINE);
		lbl.setText("Ok");*/
		
		final List list = new List(shell, SWT.MULTI);
		list.add("a");
		list.add("b");
		list.add("c");
		
		System.out.println(list.getItemCount());
		System.out.println(list.indexOf("b"));
		list.selectAll();
		
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//System.out.println(list.getSelectionCount());
				for(String s : list.getSelection())
					System.out.print(s);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("...selected");
			}
			
			
		});
		
		
		Table table = new Table(shell, SWT.SINGLE|SWT.BORDER|SWT.FULL_SELECTION|SWT.CHECK);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn col1 = new TableColumn(table, SWT.RESIZE);
		col1.setText("Seq");
		col1.setAlignment(SWT.CENTER);
		col1.setToolTipText("序号");
		col1.setMoveable(true);
		col1.pack();
		
		TableColumn col2 = new TableColumn(table, SWT.RESIZE);
		col2.setText("Name");
		col2.setAlignment(SWT.CENTER);
		col2.setToolTipText("姓名");
		col2.setMoveable(true);
		Image image = new Image(display, UI.class.getClassLoader().getResourceAsStream("aoesoft/pingyin/logo.png"));
		col2.setImage(image);
		col2.pack();
		
		
		TableItem table1= new TableItem(table, SWT.NONE);
		table1.setText(new String[]{"1", "Helo"});
		
		TableItem tableItem2= new TableItem(table, SWT.NONE);
		tableItem2.setText(new String[]{"2", "AOE"});
		tableItem2.setChecked(true);
		
		final Tree tree = new Tree(shell, SWT.SINGLE|SWT.CHECK);
		
		TreeItem tr1 = new TreeItem(tree, SWT.NONE);
		tr1.setText("Root");
		
		TreeItem tr2= new TreeItem(tr1, SWT.CHECK);
		tr2.setText("南京");
		
		TreeItem tr3 = new TreeItem(tr1, SWT.NONE);
		tr3.setText("连云港");
		
		TreeItem tr21= new TreeItem(tr2, SWT.CHECK);
		tr21.setText("浦口");
		
		
		final Combo combo = new Combo(shell, SWT.DROP_DOWN);
		combo.add("a");
		combo.add("b");
		combo.add("c");
		combo.setItems(new String[]{"x", "y", "z"});
		
		combo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent mouseevent) {
				System.out.println(combo.getText());
			}
		});
		
		Composite com = new Composite(shell, SWT.V_SCROLL|SWT.BORDER);
		com.setBounds(0, 0, 100, 20);
		com.setLayout(new FillLayout());
		
		Label lbl1 = new Label(com, SWT.ICON_ERROR);
		lbl1.setText("AA");
		Label lbl2 = new Label(com, SWT.ICON_QUESTION);
		lbl2.setText("BB");
		
		TabFolder tabFolder = new TabFolder(com, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("区域");
		
		Group group = new Group(tabFolder, SWT.NONE);
		group.setText("设置");
		
		tabItem.setControl(group);
		
		
		//---
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
		shell.dispose();
	}
}
