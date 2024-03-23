package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class itemController {
	
	
	// Search 
    @FXML
    private TextField SearchField;

    @FXML
    private Button searchButton;

    // Item Table 
    @FXML
    private TableView<Item> itemTableData;

    @FXML
    private TableColumn<Item, Integer> par_codeColumn;

    @FXML
    private TableColumn<Item, String> item_nameColumn;

    @FXML
    private TableColumn<Item, Double> Price;

    @FXML
    private TableColumn<Item, Integer> QntyClmn;

    // Buttons 
    @FXML
    private Button RefreshBtn;

    @FXML
    private Button BackButton;

    // Update 
    @FXML
    private TextField oldParcode;

    @FXML
    private TextField updatedID;

    @FXML
    private TextField updatedName;

    @FXML
    private TextField updatedPrice;

    @FXML
    private TextField updatedQnty;

    @FXML
    private Button UpdateBtn;

    // Delete 
    @FXML
    private TextField DeleteParcode;

    @FXML
    private Button DeleteBtn;

    // Add Section
    @FXML
    private TextField Addprice;

    @FXML
    private TextField addName;

    @FXML
    private TextField addQuantity;

    @FXML
    private TextField AddItemId;

    @FXML
    private Button addBtn;

    // Data 
    private ArrayList<Item> data;
    private ObservableList<Item> dataList;
	
    
    
    
    @FXML
    void SearchOnAction(ActionEvent event) {
        String searchText = SearchField.getText();
        FilteredList<Item> filteredData = new FilteredList<>(dataList, item -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            if (String.valueOf(item.getItem_id()).toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else return item.getItem_name().toLowerCase().contains(lowerCaseFilter);
        });
        
        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTableData.comparatorProperty());
        itemTableData.setItems(sortedData);
    }

    
	@FXML
	public void updateOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		// TODO Autogenerated
		try {
		if ( oldParcode.getText() != null) {
			connector.a.connectDB();
			
			if ( updatedName.getText() != null)
			updateitemName(Integer.parseInt(oldParcode.getText()),updatedName.getText());
			
			if ( updatedPrice.getText() != null)
			updateprice(Integer.parseInt(oldParcode.getText()),Double.parseDouble(updatedPrice.getText()));
			
			
			if ( updatedQnty.getText() != null)
			updateQnty(Integer.parseInt(oldParcode.getText()),Integer.parseInt(updatedQnty.getText()));
			
			
			if ( updatedID.getText()!= null)
			updateitemID(Integer.parseInt(oldParcode.getText()),Integer.parseInt(updatedID.getText()));
			
			oldParcode.clear();
			updatedName.clear();
			updatedID.clear();
			updatedPrice.clear();
			updatedQnty.clear();
			updatedID.clear();
			
			initialize();
			
		}
		}
		catch (Exception e) {
			oldParcode.clear();
			updatedName.clear();
			updatedID.clear();
			updatedPrice.clear();
			updatedQnty.clear();
			updatedID.clear();
			showDialog("ERROR","Wrong input!"," check your input again",AlertType.ERROR);
		}
		
	}
	
	// Event Listener on Button[#DeleteBtn].onAction
	@FXML
	public void deleteOnAction(ActionEvent event) {
		// TODO Autogenerated

		try {
			if (! DeleteParcode.getText().equals("")) {
				connector.a.connectDB();
				String sql = "delete from items where items.item_id = " + Integer.parseInt(DeleteParcode.getText()) + ";";
				PreparedStatement ps = (PreparedStatement) connector.a.connectDB().prepareStatement(sql);
				ps.execute();
				 DeleteParcode.clear();
				 initialize();
			}
			
		}
	 catch (Exception e) {
		 DeleteParcode.clear();
		 showDialog("ERROR","Wrong input!"," check your input again",AlertType.ERROR);
	 }
		
	}
	@FXML
	
	public void addOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		// TODO Autogenerated

		try {
			Item rc;
			rc = new Item(Integer.parseInt(AddItemId.getText()),addName.getText(),Double.parseDouble(Addprice.getText()),
					Integer.parseInt(addQuantity.getText()));
			

			
			Item.it = rc;
			insertData(rc);
			
			AddItemId.clear();
			addName.clear();
			addQuantity.clear();
			Addprice.clear();
			initialize();
			
			
		} catch (Exception e) {
			showDialog("", "Wrong input!!", "Please check the input again", AlertType.ERROR);
		}
		
		
		
	}
	
	// Event Listener on Button[#BackButton].onAction
	@FXML
	public void backOnAction(ActionEvent event) throws IOException {
		// TODO Autogenerated
		Stage stage;
		Parent root;
		stage = (Stage) BackButton.getScene().getWindow();
		stage.close();
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		Scene scene = new Scene(root, 901, 649);
		stage.setScene(scene);
		//stage.setTitle("Choose order");
		stage.show();
		stage.setMaximized(false);

		
	}
	// Event Listener on Button[#RefreshBtn].onAction
	@FXML
	public void refreshOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		// TODO Autogenerated
		
		oldParcode.clear();
		updatedName.clear();
		updatedID.clear();
		updatedPrice.clear();
		updatedQnty.clear();
		updatedID.clear();
		
		DeleteParcode.clear();
		 
		 AddItemId.clear();
			addName.clear();
			addQuantity.clear();
			Addprice.clear();
			SearchField.clear();
		    			
			initialize();
		
		
		
	}
	
	//@Override
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		data = new ArrayList<>();
		dataList = FXCollections.observableArrayList(data);
		itemTableData.setEditable(true);
		par_codeColumn.setCellValueFactory(new PropertyValueFactory<Item,Integer>("item_id"));
		item_nameColumn.setCellValueFactory(new PropertyValueFactory<Item,String>("item_name"));
		Price.setCellValueFactory(new PropertyValueFactory<Item,Double>("item_price"));
		QntyClmn.setCellValueFactory(new PropertyValueFactory<Item,Integer>("quantity"));
		
		
		try {
		getData();
		}
		catch (Exception e) {
			 showDialog("ERROR","Wrong input!"," check your input again",AlertType.ERROR);
		 }
		itemTableData.setItems(dataList);		
	}
	
	
	private void updateitemName (int id, String nn) throws ClassNotFoundException, SQLException {
		if ( updatedName.getText() != null) {
			connector.a.connectDB();
			connector.a.ExecuteStatement(
					"update  items set item_name = '" + nn + "' where item_id = " + Integer.parseInt(oldParcode.getText()) + ";");
			connector.a.connectDB().close();
			}
		
	}
	
	private void updateitemID (int id, Integer newID) throws ClassNotFoundException, SQLException {
		if ( updatedID.getText()!= null) {
			connector.a.connectDB();
			connector.a.ExecuteStatement(
					"update  items set item_id = " + newID + " where item_id = " + Integer.parseInt(oldParcode.getText()) + ";");
			connector.a.connectDB().close();
			}	
		
	}
	
	
	
	
	private void updateprice (int id, Double nn) throws ClassNotFoundException, SQLException {
		if ( updatedPrice.getText() != null) {
			connector.a.connectDB();
			connector.a.ExecuteStatement(
					"update  items set item_price = " + nn + " where item_id = " + Integer.parseInt(oldParcode.getText()) + ";");
			connector.a.connectDB().close();
			}
		
	}
	
	
	private void updateQnty (int id, int nn) throws ClassNotFoundException, SQLException {
		if ( updatedQnty.getText() != null) {
			connector.a.connectDB();
			connector.a.ExecuteStatement(
					"update  items set quantity = " + nn + " where item_id = " + Integer.parseInt(oldParcode.getText()) + ";");
			connector.a.connectDB().close();
			}
		
	}
	
	
	public void getData() throws ClassNotFoundException, SQLException {
		String sql = "select * from items;";
		
		try {
		connector.a.connectDB();
		
		java.sql.Statement statement2 = connector.a.connectDB().createStatement();
		ResultSet ResSet = statement2.executeQuery(sql);
		
		while (ResSet.next()) {
			Item i = new Item(ResSet.getInt(1),ResSet.getString(2),ResSet.getDouble(3),ResSet.getInt(4));
			dataList.add(i);

		}
		ResSet.close();
		statement2.close();
		connector.a.connectDB().close();
		}
		 catch (Exception e) {
			 showDialog("ERROR","Wrong input!"," check your input again",AlertType.ERROR);
		 }
	}
	
	
	
	public void showDialog(String title, String header, String body, AlertType type) {
		Alert alert = new Alert(type); 
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		alert.show();

	}
	
	
	private void insertData(Item rc) {

		try {
		

			connector.a.connectDB();
			String sql = "insert into items(item_id,item_name ,item_price,quantity) value (?,?,?,?)";
			PreparedStatement ps = (PreparedStatement) connector.a.connectDB().prepareStatement(sql);
			ps.setInt(1,rc.getItem_id());
			ps.setString(2,rc.getItem_name());
			ps.setDouble(3, rc.getItem_price());
			ps.setInt(4, rc.getQuantity());
		
			
			
			ps.execute();
			
		} catch (SQLException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
	}
	
}


