package application;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;


public class ordersController {
	
	public static int cusId = 0;
	public static String cusName = "";
	public static int orderId;
	public static double priceToShow = 0;
	private ArrayList<invoiceData> data;
	private ObservableList<invoiceData> dataList;
	boolean isCreatOrder = false;
	
	
	
    @FXML
    private TableView<invoiceData> TableData;

    @FXML
    private Button addItem;

    @FXML
    private Button btnback;

    @FXML
    private Button cancelOrdre;

    @FXML
    private Button addc;
    
    @FXML
    private Button deleteItem;
    
    
    
    @FXML
    private TableColumn<invoiceData, Integer> counter;

    
    
    @FXML
    private TextField cusName1;
    @FXML
    private TextField cusphone;

    

  

    @FXML
    private TableColumn<invoiceData, String> itemName;

    @FXML
    private TableColumn<invoiceData, Integer> itemParcode;

    @FXML
    private TableColumn<invoiceData, Double> itemPrice;

    @FXML
    private TableColumn<invoiceData, Integer> itemQuantity;
    
    @FXML
    private TableColumn<invoiceData, Integer> itembyEmployee;
    
    

    @FXML
    private Label orderID;
    
   
    @FXML
    private Label price;

 
    @FXML
    private TextField search;
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDateTime now = LocalDateTime.now();
	
	
	
    @FXML
    void CancelOrder(ActionEvent event) {

    	try {
			
				connector.a.connectDB();
				connector.a.ExecuteStatement("delete from  invoice where order_id =" + orderId + ";");
				connector.a.ExecuteStatement("delete from  orders where order_id =" + orderId + ";");
				connector.a.ExecuteStatement("delete from  customers where customer_id =" + cusId + ";");
				connector.a.connectDB().close();
			

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { // open new stage
			Stage stage;
			Parent root;
			stage = (Stage) cancelOrdre.getScene().getWindow();
			stage.close();
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root, 901, 649);
			stage.setScene(scene);
			stage.setTitle("Choose One");
			stage.show();
			stage.setMaximized(false);


		} catch (IOException e1) {

		}
	}
    
    @FXML
    void addCust(ActionEvent event) {
        try {
            // Validate customer name to be a non-numeric string (assuming names can have spaces and certain characters like apostrophes)
            String customerName = cusName1.getText().trim();
            if (customerName.isEmpty() || !customerName.matches("[A-Za-z ']+")) {
                Message.displayMassage("Invalid Customer Name. Please enter a valid name.", "error");
                return;
            }

            // Validate customer phone to be a number
            String customerPhone = cusphone.getText().trim();
            if (!customerPhone.matches("\\d+")) {
                Message.displayMassage("Invalid Customer Phone. Please enter a numeric phone number.", "error");
                return;
            }

            // If validations pass, proceed to create Customer object and insert data
            Customer cs = new Customer(customerName, customerPhone);
            Customer.cust = cs;
            insertData(cs);
            initialize();
            cusName1.clear();
            cusphone.clear();

            // Show confirmation alert using the showDialog method
            showDialog("Success", null, "Customer added successfully.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            Message.displayMassage("An error occurred. Please try again.", "error");
        }
    }

    
	private void insertData(Customer cs) {
		
		PreparedStatement st1,st2;
		try {
		connector.a.connectDB();
		String sql = "Insert into customers (customer_name,customer_phone,table_number) values(?,?,?)";
		PreparedStatement ps = (PreparedStatement) connector.a.connectDB().prepareStatement(sql);
		ps.setString(1, cs.getCustomer_name());
		ps.setString(2, cs.getCustomer_phone());
		//ps.setInt(3, 1);

		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		ps.setInt(3, randomNumber);
		ps.execute();
		
		

		
		st1 = connector.a.connectDB().prepareStatement("select MAX(customer_id) from customers;");
		ResultSet r2 = st1.executeQuery();
		if (r2.next()) {
			cusId = r2.getInt(1);			
			
			String sql1 = "insert into orders(customer_id) value(?);";
			
			PreparedStatement ps1 = (PreparedStatement) connector.a.connectDB().prepareStatement(sql1);
			ps1.setInt(1, cusId);
			ps1.execute();
			
			st2 = connector.a.connectDB().prepareStatement("select customer_name from customers where customer_id =" +cusId +";");
			ResultSet r3 = st2.executeQuery();
			if (r3.next()) {
			cusName = r3.getString(1);
			}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

    @FXML
    void addNew(ActionEvent event) {
    	Scene scene;
		Stage stage = null ;
		
		try { // open new stage
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("addItemTOrder.fxml"));
			scene = new Scene(fxmlLoader.load(), 394, 598);
			stage = new Stage();
			stage.setTitle("add new item");
			stage.setScene(scene);
			stage.show();
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent paramT) {

				initialize();

			}
		});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
    }

    @FXML
    void back(ActionEvent event) {
		try { // open new stage
			Stage stage;
			Parent root;
			stage = (Stage) btnback.getScene().getWindow();
			stage.close();
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root, 901, 649);
			stage.setScene(scene);
			stage.setTitle("Choose One");
			stage.show();
			stage.setMaximized(false);

		} catch (IOException e1) {

		}

    }

    @FXML
    void deleteItem(ActionEvent event) {
    	ObservableList<invoiceData> selectedRows = TableData.getSelectionModel().getSelectedItems();
		ArrayList<invoiceData> rows = new ArrayList<>(selectedRows);
		if (rows.size() == 0) {
			return;
		}
		deleteRow(rows.get(0));

	}

    
	private void deleteRow(invoiceData row) {
		priceToShow -= (row.getPrice() * row.getQuantity());


		try {
			connector.a.connectDB();

			connector.a.ExecuteStatement("delete from invoice where  order_id=" + row.getOrder_id() + " and item_id="
					+ row.getItem_id() + ";");
			PreparedStatement st2 = connector.a.connectDB()
					.prepareStatement("select * from items where item_id=" + row.getItem_id() + ";");
			ResultSet r2 = st2.executeQuery();
			if (r2.next()) {

			}
			connector.a.ExecuteStatement("update items set quantity = " + (r2.getInt(4) + row.getQuantity())
					+ " where item_id = " + r2.getInt(1) + ";");

			connector.a.connectDB().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		initialize();
    }
	
	
	
	private void updateQuantity(int id1, int id2, int newValue, int oldValue) {
		try {
			connector.a.connectDB();

			PreparedStatement st2 = connector.a.connectDB()
					.prepareStatement("select * from item where item_id=" + id1 + ";");
			ResultSet r2 = st2.executeQuery();
			if (r2.next()) {
			}

			if (r2.getInt(4) - (newValue - oldValue) >= 0) {
				connector.a.ExecuteStatement("update invoice set quantity = " + newValue + " where order_id = " + id2
						+ " and item_id = " + id1 + ";");
				connector.a.ExecuteStatement("update item set quantity = " + (r2.getInt(4) - (newValue - oldValue))
						+ " where item_id = " + r2.getInt(1) +  ";");
			} else {
				if (r2.getInt(4) > 0) {
					Message.displayMassage(
							"There is not enough quantity of this product!\r\n" + "There is only: " + r2.getInt(4),
							"error");
				} else {
					Message.displayMassage("out of stock !", "error");

				}
			}

			connector.a.connectDB().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		initialize();

	}
	
    
    private void searchRentalEmployee() {
		FilteredList<invoiceData> filteredData = new FilteredList<>(dataList, b -> true);
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(invoice -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(invoice.getItem_id()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // matches car Id
				} else if (invoice.getItemName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // matches password
				} else
					return false; // Does not match.
			});
		});
		SortedList<invoiceData> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(TableData.comparatorProperty());
		TableData.setItems(sortedData);
	}

    
    @FXML
	public void initialize() {
    	
    	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date myDate = null;
		@SuppressWarnings("unused")
		java.sql.Date sqlDate;
		try {
			myDate = formatter.parse(dtf.format(now));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sqlDate = new java.sql.Date(myDate.getTime());

		
			if (!isCreatOrder) {
				PreparedStatement st2;
				try {
					if (!cusName.equals("")) {
					connector.a.connectDB();
					String sql = "insert into orders(emp_id,customer_id,order_date) value(?,?,?);";
					PreparedStatement ps = (PreparedStatement) connector.a.connectDB().prepareStatement(sql);
					ps.setInt(1, Sign_inController.empId);
					ps.setInt(2, cusId);
					ps.setTimestamp(3, new java.sql.Timestamp(myDate.getTime()));

					ps.execute();
					} else {
						connector.a.connectDB();
						String sql = "insert into orders(emp_id,order_date) value(?,?);";
						PreparedStatement ps = (PreparedStatement) connector.a.connectDB().prepareStatement(sql);
						ps.setInt(1,Sign_inController.empId);
						ps.setTimestamp(2, new java.sql.Timestamp(myDate.getTime()));

						ps.execute();
						
					}
					
					st2 = connector.a.connectDB().prepareStatement("select MAX(order_id) from orders;");
					ResultSet r2 = st2.executeQuery();
					if (r2.next()) {
						orderId = r2.getInt(1);
					
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				isCreatOrder = true;
			}
		
		orderID.setText(String.valueOf(orderId));
	
			
		
		data = new ArrayList<>();
		dataList = FXCollections.observableArrayList(data);
		TableData.setEditable(true);
		counter.setSortable(false);
		counter.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Integer>(TableData.getItems().indexOf(column.getValue()) + 1));
	
		itemParcode.setCellValueFactory(new PropertyValueFactory<invoiceData, Integer>("item_id"));		
		itemName.setCellValueFactory(new PropertyValueFactory<invoiceData, String>("itemName"));
		itemQuantity.setCellValueFactory(new PropertyValueFactory<invoiceData, Integer>("quantity"));
		itemQuantity.setCellFactory(TextFieldTableCell.<invoiceData, Integer>forTableColumn(new IntegerStringConverter()));
		itemQuantity.setOnEditStart((CellEditEvent<invoiceData, Integer> t) -> {
			int old = t.getOldValue();
			((invoiceData) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue()); // display
			// only
			System.out.println(t.getRowValue().getItem_id());

			updateQuantity(t.getRowValue().getItem_id(), t.getRowValue().getOrder_id(), t.getNewValue(), old);
		});

		itemPrice.setCellValueFactory(new PropertyValueFactory<invoiceData, Double>("price"));

		itembyEmployee.setCellValueFactory(new PropertyValueFactory<invoiceData, Integer>("emp_id"));
		getData();
		price.setText(priceToShow + " $");
		TableData.setItems(dataList);

		searchRentalEmployee();

	}

	public void getData() {
		priceToShow = 0;
		String SQL, od;
		
		
		
			SQL = "select * from invoice where order_id=" + orderId + ";";
			od = "select * from orders where order_id=" + orderId + ";";
		
		try {
			connector.a.connectDB();
			java.sql.Statement state = connector.a.connectDB().createStatement();
			ResultSet rs = state.executeQuery(SQL);

			while (rs.next()) {
			
				String SQL2 = "select * from items where item_id=" + rs.getInt(3) + ";";
				java.sql.Statement state2 = connector.a.connectDB().createStatement();
				ResultSet rs2 = state2.executeQuery(SQL2);

				if (rs2.next()) {
					java.sql.Statement state3 = connector.a.connectDB().createStatement();
					ResultSet rs3 = state3.executeQuery(od);
					if (rs3.next()) {
						System.out.println("price "+rs.getDouble(5));
						
						System.out.println("emp "+rs3.getInt(4));
						
						System.out.println("item name "+rs2.getString(2));

					invoiceData it = new invoiceData(orderId, rs.getInt(4),
							
							rs.getDouble(5),
							
							
							rs2.getInt(1),
							
							rs2.getString(2),
							
							rs3.getInt(4),
							
							rs.getString(2));
					
						dataList.add(it);
						priceToShow += (rs.getDouble(5) * rs.getInt(4));
					}
				}
			}

			rs.close();
			state.close();
			connector.a.connectDB().close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showDialog(String title, String header, String body, AlertType type) {
		Alert alert = new Alert(type); 
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		alert.show();

	}
	
	
}
