package tabletest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;
 import java.util.concurrent.ThreadLocalRandom;

public class ProduListTableController implements Initializable {

    @FXML
    private TableView<ProductTable> table;
    @FXML
    private TableColumn<ProductTable, String> product;
    @FXML
    private TableColumn<ProductTable, String> price;
    @FXML
    private TableColumn<ProductTable, Integer> quantity;
    private TextField inputID;
    @FXML
    private TextField inputProduct;
    @FXML
    private TextField inputPrice;
    @FXML
    private TextField inputQuantity;
    @FXML
    private TextField Search;

    
    private final ObservableList<ProductTable> datalist=FXCollections.observableArrayList();
    File saveFile = new File("save_here.txt");
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        product.setCellValueFactory(new PropertyValueFactory<ProductTable,String>("prodName"));
        price.setCellValueFactory(new PropertyValueFactory<ProductTable,String>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<ProductTable,Integer>("quantity"));
        
        ProductTable p1 = new ProductTable("Sample Product","₱15.00",56);
        ProductTable p2 = new ProductTable("Product","₱65.00",30);
        datalist.addAll(p1,p2);
        editData();
        searchData();
        
        
    }    
    @FXML
    private void delete(ActionEvent event) {
        TableView.TableViewSelectionModel<ProductTable> selectionModel = table.getSelectionModel();
        if (selectionModel.isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Select Data Before Deleting");
            a.showAndWait();
            return;
            
        }else{
            ObservableList<Integer> list = selectionModel.getSelectedIndices();
            Integer[] selectedIndices = new Integer[list.size()];
            selectedIndices=list.toArray(selectedIndices);
            
            Arrays.sort(selectedIndices);
            
            for(int i = selectedIndices.length -1; i>=0;i--){
                selectionModel.clearSelection(selectedIndices[i].intValue());//
                datalist.remove(selectedIndices[i].intValue());
            }
        }
    }


    @FXML
    private void Insert(ActionEvent event){ 
        DecimalFormat n =new DecimalFormat("'₱'0.00");
        try{
            
            
        String prodName=inputProduct.getText();
        double price=Double.parseDouble(inputPrice.getText());
        int quantity=Integer.parseInt(inputQuantity.getText());
        ProductTable newData = new ProductTable(prodName,n.format(price),quantity);
        
        datalist.addAll(newData);
        
        inputProduct.clear();
        inputPrice.clear();
        inputQuantity.clear();
        }
        catch(Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Datatype/Incomplete Fields");
            a.showAndWait();
            return;
        }
        
    }
    
        private void searchData(){
            FilteredList <ProductTable> filteredData = new FilteredList<>(datalist, b -> true);
            
            Search.textProperty().addListener((observable,oldValue,newValue) ->  {
                filteredData.setPredicate(Product ->{
                
                    if (newValue == null||newValue.isEmpty()){
                        return true; //return table back
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (Product.getProdName().toLowerCase().indexOf(lowerCaseFilter)!=-1){
                        return true;
                    }

                    else if (String.valueOf(Product.getPrice()).indexOf(lowerCaseFilter)!=-1){
                        return true;
                    }
                    else if (String.valueOf(Product.getQuantity()).indexOf(lowerCaseFilter)!=-1){
                        return true;
                    }else { // returning false leaves it blank as long as input is still selected
                    return false;
                    }
                
                });
             
            });
            
            SortedList<ProductTable> sortedData = new SortedList<>(filteredData);
            
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
           
            
        }
    
        private void editData(){
            DecimalFormat n =new DecimalFormat("'₱'0.00");
        
        product.setCellFactory(TextFieldTableCell.<ProductTable>forTableColumn());
        product.setOnEditCommit(event->{
            ProductTable product = event.getTableView().getItems().get(event.getTablePosition().getRow());
            product.setProdName(event.getNewValue());
            System.out.println(product.getProdName()+"' was updated to "+event.getNewValue()+" at row "+(event.getTablePosition().getRow()+1));
        });
        try{
             price.setCellFactory(TextFieldTableCell.<ProductTable>forTableColumn());
            price.setOnEditCommit(event->{
            ProductTable product = event.getTableView().getItems().get(event.getTablePosition().getRow());
            double priceSet = Double.parseDouble(event.getNewValue());
            n.setDecimalSeparatorAlwaysShown(true);   
            product.setPrice(n.format(priceSet));
            n.setDecimalSeparatorAlwaysShown(true);   
            System.out.println(product.getProdName()+"'s Price was updated to "+n.format(priceSet)+" at row "+(event.getTablePosition().getRow()+1));
            price.setCellFactory(TextFieldTableCell.<ProductTable>forTableColumn());
        }); 
            
        }   
        catch (Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Datatype for price, insert number only");
            a.showAndWait();    
            return;
        }
        try{
             quantity.setCellFactory(TextFieldTableCell.<ProductTable,Integer>forTableColumn(new IntegerStringConverter()));
            quantity.setOnEditCommit(event->{
            ProductTable product = event.getTableView().getItems().get(event.getTablePosition().getRow());
            product.setQuantity(event.getNewValue());
            System.out.println(product.getProdName()+"'s Count was updated to "+event.getNewValue()+" at row "+(event.getTablePosition().getRow()+1));
        }); 
            
        }   
        catch (Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Datatype for quantity, insert number only");
            a.showAndWait();    
            return;
        }
        
       
        
        
        
    }


    @FXML
    private void inputSwitch2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            inputPrice.requestFocus();
        }
    }

    @FXML
    private void inputSwitch3(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            inputQuantity.requestFocus();
        }
        
    }

    @FXML
    private void Insert2(KeyEvent event) {
        DecimalFormat n =new DecimalFormat("'₱'0.00");
        if (event.getCode().equals(KeyCode.ENTER)){
            try{                        
            String prodName=inputProduct.getText();
            double price=Double.parseDouble(inputPrice.getText());
            int quantity=Integer.parseInt(inputQuantity.getText());
            
            ProductTable newData = new ProductTable(prodName,n.format(price),quantity);

            datalist.addAll(newData);      
            inputProduct.clear();
            inputPrice.clear();
            inputQuantity.clear();
        }
            catch(Exception e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid Datatype/Incomplete Fields");
                a.showAndWait();
                return;
            }
                inputProduct.requestFocus();
            
        }
        
    }

    @FXML
    private void randomInsert(ActionEvent event) {
        DecimalFormat n =new DecimalFormat("'₱'0.00");
        
        String prodName=randomProd();
        double price = randomPrice();
        int quantity = randomNumber();
        String priceM=n.format(price);
        ProductTable newData = new ProductTable(prodName,priceM,quantity);
         
        
        datalist.addAll(newData);
    }
    
    public String randomProd() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringer = new StringBuilder();
        Random rnd = new Random();
        int cL=randomNameLength();
        while (stringer.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            stringer.append(SALTCHARS.charAt(index));
        }
        String saltStr = "Product "+stringer.toString();
        return saltStr;

    }
    public double randomPrice(){
        double rangeMin=1.00;
        double rangeMax=999.99;
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return randomValue;
    }
    public int randomNumber(){
        int min=1;
        int max=999;

        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
     public int randomNameLength(){
        int rangeMin=4;
        int rangeMax=15;
        Random r = new Random();
        int randomValue = rangeMin + (rangeMax - rangeMin) * r.nextInt();
        return randomValue;
    }

    @FXML
    private void saveData(ActionEvent event) throws Exception {
        
        
            Writer writer = null;
         try {
             File file = new File(System.getProperty("user.home") + "/Desktop"+"/ProductTable.csv");
             writer = new BufferedWriter(new FileWriter(file));
             
             for (ProductTable table : datalist) {
                    
                 String text =table.getProdName() + "," + table.getPrice() + "," + table.getQuantity() + "\n";
                 System.out.println(text);
                 writer.write(text);
             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         finally {

             writer.flush();
              writer.close();
         } 
     }
   

    @FXML
    private void loadData(ActionEvent event) throws Exception{
        String file = System.getProperty("user.home") + "/Desktop"+"/ProductTable.csv";
        BufferedReader reader =null;
        String line = "";
        datalist.removeAll(datalist);
        try{
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine())!=null){
                
                String[] row = line.split(",");
                String product = row[0];
                String price = row[1];
                int quantity = Integer.parseInt(row[2]);
                ProductTable newData = new ProductTable(product,price,quantity);
                datalist.addAll(newData);
            }
            
        }
        catch (Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Save file not Found");
            a.showAndWait();    
            return;
            
        }
        finally{
            reader.close();
        }
        
        
    }

    @FXML
    private void deletAll(ActionEvent event) {
        datalist.removeAll(datalist);
    }


        
        

    
    
}
