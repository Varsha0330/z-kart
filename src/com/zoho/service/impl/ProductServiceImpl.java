package com.zoho.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zoho.beans.DemandBean;
import com.zoho.beans.ProductBean;
import com.zoho.service.ProductService;
import com.zoho.utility.DBUtil;
import com.zoho.utility.IDUtil;
import com.zoho.utility.MailMessage;

public class ProductServiceImpl implements ProductService {

	@Override
	public String addProduct(String brand, String category, String modal, double price, int stock,
			int discount, InputStream prodImage) {
		String status = null;
		String prodId = IDUtil.generateId();

		ProductBean product = new ProductBean(prodId, brand, category, modal, price, stock, discount, prodImage);

		status = addProduct(product);

		return status;
	}

	@Override
	public String addProduct(ProductBean product) {
		String status = "Product Registration Failed!";

		if (product.getProdId() == null)
			product.setProdId(IDUtil.generateId());

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into product values(?,?,?,?,?,?,?,?);");
			ps.setString(1, product.getProdId());
			ps.setString(2, product.getBrand());
			ps.setString(3, product.getCategory());
			ps.setString(4, product.getModal());
			ps.setDouble(5, product.getPrice());
			ps.setInt(6, product.getStock());
			ps.setInt(7, product.getDiscount());
			ps.setBlob(8, product.getProdImage());

			int k = ps.executeUpdate();

			if (k > 0) {

				status = "Product Added Successfully with Product Id: " + product.getProdId();

			} else {

				status = "Product Updation Failed!";
			}

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return status;
	}

	@Override
	public String removeProduct(String prodId) {
		String status = "Product Removal Failed!";

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		try {
			ps = con.prepareStatement("delete from product where pid=?");
			ps.setString(1, prodId);

			int k = ps.executeUpdate();

			if (k > 0) {
				status = "Product Removed Successfully!";

				ps2 = con.prepareStatement("delete from usercart where prodid=?");

				ps2.setString(1, prodId);

				ps2.executeUpdate();

			}

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(ps2);

		return status;
	}

	@Override
	public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct) {
		String status = "Product Updation Failed!";

		if (!prevProduct.getProdId().equals(updatedProduct.getProdId())) {

			status = "Both Products are Different, Updation Failed!";

			return status;
		}

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(
					"update product set brand=?,category=?,modal=?,price=?,stock=?,discount=?,image=? where pid=?");

			ps.setString(1, updatedProduct.getBrand());
			ps.setString(2, updatedProduct.getCategory());
			ps.setString(3, updatedProduct.getModal());
			ps.setDouble(4, updatedProduct.getPrice());
			ps.setInt(5, updatedProduct.getStock());
			ps.setInt(6, updatedProduct.getDiscount());
			ps.setBlob(7, updatedProduct.getProdImage());
			ps.setString(8, prevProduct.getProdId());

			int k = ps.executeUpdate();
			
			//System.out.println(k);

			if (k > 0)
				status = "Product Updated Successfully!";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return status;
	}

	@Override
	public String updateProductPrice(String prodId, double updatedPrice) {
		String status = "Price Updation Failed!";

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("update product set price=? where pid=?");

			ps.setDouble(1, updatedPrice);
			ps.setString(2, prodId);

			int k = ps.executeUpdate();

			if (k > 0)
				status = "Price Updated Successfully!";
		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return status;
	}

	@Override
	public List<ProductBean> getAllProducts() {
		List<ProductBean> products = new ArrayList<ProductBean>();

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from product");

			rs = ps.executeQuery();

			while (rs.next()) {

				ProductBean product = new ProductBean();

				product.setProdId(rs.getString(1));
				product.setBrand(rs.getString(2));
				product.setCategory(rs.getString(3));
				product.setModal(rs.getString(4));
				product.setPrice(rs.getDouble(5));
				product.setStock(rs.getInt(6));
				product.setDiscount(rs.getInt(7));
				product.setProdImage(rs.getAsciiStream(8));

				products.add(product);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return products;
	}

	@Override
	public List<ProductBean> getAllProductsByType(String type) {
		List<ProductBean> products = new ArrayList<ProductBean>();

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("SELECT * FROM `shopping-cart`.product where lower(category) like ?;");
			ps.setString(1, "%" + type + "%");
			rs = ps.executeQuery();

			while (rs.next()) {

				ProductBean product = new ProductBean();

				product.setProdId(rs.getString(1));
				product.setBrand(rs.getString(2));
				product.setCategory(rs.getString(3));
				product.setModal(rs.getString(4));
				product.setPrice(rs.getDouble(5));
				product.setStock(rs.getInt(6));
				product.setStock(rs.getInt(7));
				product.setProdImage(rs.getAsciiStream(8));

				products.add(product);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return products;
	}

	@Override
	public List<ProductBean> searchAllProducts(String search) {
		List<ProductBean> products = new ArrayList<ProductBean>();

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement(
					"SELECT * FROM `shopping-cart`.product where lower(category) like ? or lower(brand) like ? or lower(modal) like ?");
			search = "%" + search + "%";
			ps.setString(1, search);
			ps.setString(2, search);
			ps.setString(3, search);
			rs = ps.executeQuery();

			while (rs.next()) {

				ProductBean product = new ProductBean();

				product.setProdId(rs.getString(1));
				product.setBrand(rs.getString(2));
				product.setCategory(rs.getString(3));
				product.setModal(rs.getString(4));
				product.setPrice(rs.getDouble(5));
				product.setStock(rs.getInt(6));
				product.setStock(rs.getInt(7));
				product.setProdImage(rs.getAsciiStream(8));

				products.add(product);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return products;
	}

	@Override
	public byte[] getImage(String prodId) {
		byte[] image = null;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select image from product where  pid=?");

			ps.setString(1, prodId);

			rs = ps.executeQuery();

			if (rs.next())
				image = rs.getBytes("image");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return image;
	}

	@Override
	public ProductBean getProductDetails(String prodId) {
		ProductBean product = null;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from product where pid=?");

			ps.setString(1, prodId);
			rs = ps.executeQuery();

			if (rs.next()) {
				product = new ProductBean();
				product.setProdId(rs.getString(1));
				product.setBrand(rs.getString(2));
				product.setCategory(rs.getString(3));
				product.setModal(rs.getString(4));
				product.setPrice(rs.getDouble(5));
				product.setStock(rs.getInt(6));
				product.setStock(rs.getInt(7));
				product.setProdImage(rs.getAsciiStream(8));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return product;
	}

	@Override
	public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct) {
		String status = "Product Updation Failed!";

		if (!prevProductId.equals(updatedProduct.getProdId())) {

			status = "Both Products are Different, Updation Failed!";

			return status;
		}

		int prevQuantity = new ProductServiceImpl().getProductQuantity(prevProductId);
		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("update product set brand=?,category=?,modal=?,price=?, stock=?,dicount=? where pid=?");

			ps.setString(1, updatedProduct.getBrand());
			ps.setString(2, updatedProduct.getCategory());
			ps.setString(3, updatedProduct.getModal());
			ps.setDouble(4, updatedProduct.getPrice());
			ps.setInt(5, updatedProduct.getStock());
			ps.setInt(6, updatedProduct.getDiscount());
			ps.setString(7, prevProductId);

			int k = ps.executeUpdate();
			// System.out.println("prevQuantity: "+prevQuantity);
			if ((k > 0) && (prevQuantity < updatedProduct.getStock())) {
				status = "Product Updated Successfully!";
				// System.out.println("updated!");
				List<DemandBean> demandList = new DemandServiceImpl().haveDemanded(prevProductId);

				for (DemandBean demand : demandList) {

					String userFName = new UserServiceImpl().getFName(demand.getUserName());
					try {
						MailMessage.productAvailableNow(demand.getUserName(), userFName, updatedProduct.getBrand(),
								prevProductId);
					} catch (Exception e) {
						System.out.println("Mail Sending Failed: " + e.getMessage());
					}
					boolean flag = new DemandServiceImpl().removeProduct(demand.getUserName(), prevProductId);

					if (flag)
						status += " And Mail Send to the customers who were waiting for this product!";
				}
			} else if (k > 0)
				status = "Product Updated Successfully!";
			else
				status = "Product Not available in the store!";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		// System.out.println("Prod Update status : "+status);

		return status;
	}

	@Override
	public double getProductPrice(String prodId) {
		double price = 0;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from product where pid=?");

			ps.setString(1, prodId);
			rs = ps.executeQuery();

			if (rs.next()) {
				price = rs.getDouble("pprice");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return price;
	}

	@Override
	public boolean sellNProduct(String prodId, int n) {
		boolean flag = false;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {

			ps = con.prepareStatement("update product set stock=(stock - ?) where pid=?");

			ps.setInt(1, n);

			ps.setString(2, prodId);

			int k = ps.executeUpdate();

			if (k > 0)
				flag = true;
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return flag;
	}

	@Override
	public int getProductQuantity(String prodId) {

		int quantity = 0;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from product where pid=?");

			ps.setString(1, prodId);
			rs = ps.executeQuery();

			if (rs.next()) {
				quantity = rs.getInt("stock");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);

		return quantity;
	}

}
