package inventory.repository;


import inventory.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class InventoryRepository {

	private static String filename = "data/items.txt";
	private Inventory inventory;
	private static Logger logger= Logger.getLogger(InventoryRepository.class.getName());

	public InventoryRepository(){
		this.inventory=new Inventory();
		readParts();
		readProducts();
	}

	private void readParts(){
		ClassLoader classLoader = InventoryRepository.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		ObservableList<Part> listP = FXCollections.observableArrayList();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line=br.readLine())!=null){
				Part part=getPartFromString(line);
				if (part!=null)
					listP.add(part);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		inventory.setAllParts(listP);
	}

	private Part getPartFromString(String line){
		Part item=null;
		if (line==null|| line.equals("")) return null;
		StringTokenizer st=new StringTokenizer(line, ",");
		String type=st.nextToken();
		if (type.equals("I")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoPartId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			int idMachine= Integer.parseInt(st.nextToken());
			item = new InhousePart(id, name, price, inStock, minStock, maxStock, idMachine);
		}
		if (type.equals("O")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoPartId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			String company=st.nextToken();
			item = new OutsourcedPart(id, name, price, inStock, minStock, maxStock, company);
		}
		return item;
	}

	private void readProducts(){
		ClassLoader classLoader = InventoryRepository.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		ObservableList<Product> listP = FXCollections.observableArrayList();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line=br.readLine())!=null){
				Product product=getProductFromString(line);
				if (product!=null)
					listP.add(product);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		inventory.setProducts(listP);
	}

	private Product getProductFromString(String line){
		Product product=null;
		if (line==null|| line.equals("")) return null;
		StringTokenizer st=new StringTokenizer(line, ",");
		String type=st.nextToken();
		if (type.equals("P")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoProductId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			String partIDs=st.nextToken();

			StringTokenizer ids= new StringTokenizer(partIDs,":");
			ObservableList<Part> list= FXCollections.observableArrayList();
			while (ids.hasMoreTokens()) {
				String idP = ids.nextToken();
				Part part = inventory.lookupPart(idP);
				if (part != null)
					list.add(part);
			}
			product = new Product(id, name, price, inStock, minStock, maxStock, list);
			product.setAssociatedParts(list);
		}
		return product;
	}

	private void writeAll() {

		ClassLoader classLoader = InventoryRepository.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		ObservableList<Part> parts=inventory.getAllParts();
		ObservableList<Product> products=inventory.getProducts();

		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (Part p:parts) {
				String p2 = p.toString();
				logger.info(p2);
				bw.write(p.toString());
				bw.newLine();
			}

			for (Product pr:products) {
				StringBuilder line = new StringBuilder();
				line.append(pr.toString());
				line.append(",");
				ObservableList<Part> list = pr.getAssociatedParts();
				int index = 0;
				while (index < list.size() - 1) {
					line.append(list.get(index).getPartId());
					line.append(":");
					index++;
				}
				if (index == list.size() - 1)
					line.append(list.get(index).getPartId());
				bw.write(String.valueOf(line));
				bw.newLine();
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public void addPart(Part part){
		inventory.addPart(part);
		writeAll();
	}

	public void addProduct(Product product){
		inventory.addProduct(product);
		writeAll();
	}

	public int getAutoPartId(){
		return inventory.getAutoPartId();
	}

	public int getAutoProductId(){
		return inventory.getAutoProductId();
	}

	public ObservableList<Part> getAllParts(){
		return inventory.getAllParts();
	}

	public ObservableList<Product> getAllProducts(){
		return inventory.getProducts();
	}

	public Part lookupPart (String search){
		return inventory.lookupPart(search);
	}

	public Product lookupProduct (String search){
		return inventory.lookupProduct(search);
	}

	public void updatePart(int partIndex, Part part){
		inventory.updatePart(partIndex, part);
		writeAll();
	}

	public void updateProduct(int productIndex, Product product){
		inventory.updateProduct(productIndex, product);
		writeAll();
	}

	public void deletePart(Part part){
		inventory.deletePart(part);
		writeAll();
	}

	public void deleteProduct(Product product){
		inventory.removeProduct(product);
		writeAll();
	}

	public Inventory getInventory(){
		return inventory;
	}

	public void setInventory(Inventory inventory){
		this.inventory=inventory;
	}
}
