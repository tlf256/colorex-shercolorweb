package com.sherwin.shercolor.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherwin.shercolor.common.dao.CdsMiscCodesDao;
import com.sherwin.shercolor.common.dao.CdsProdCharzdDao;
import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.CustWebActiveProdsDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CustWebActiveProds;
import com.sherwin.shercolor.common.domain.OeServiceCdsProd;
import com.sherwin.shercolor.common.domain.OeServiceCdsProdCharzd;
import com.sherwin.shercolor.common.domain.OeServiceLargeObjectStorage;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.OeServiceSwProd;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.domain.ProductFillInfo;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;
import com.sherwin.shercolor.common.validation.ProductValidator;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	static Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductValidator productValidator;

	@Autowired
	CdsMiscCodesDao cdsMiscCodesDao;
	
	@Autowired
	CdsProdDao cdsProdDao;
	
	@Autowired
	CdsProdCharzdDao cdsProdCharzdDao;

	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;

	
	@Autowired
	ResourceBundleMessageSource messageSource;

	@Autowired
	Locale locale;	
	
	@Autowired
	CustWebActiveProdsDao activeProdsDao;
	
	
	private String oeBaseUrl;
	
	@Override
	public List<SwMessage> validateProduct(String salesNbr) throws ValidationException {
		List<SwMessage> result = new ArrayList<SwMessage>();
		// Product may be accessed by scanning UPC or typing sales number or rex number/size code.
		// Test all three that user may enter.
		try {
		// First Test - Invalid product accessed by UPC number not defined to product master file.
			salesNbr = productValidator.checkPosProdByUpc(salesNbr);
		}
		catch(ValidationException e){
			try {
		// Second Test - Invalid product accessed by sales number not defined to product master file.
			productValidator.checkPosProd(salesNbr);
			}
			catch (ValidationException e1){
		// Final Third Test - Invalid product accessed by product number and size code not defined to product master file.
		// Format for product and size code - A97W01251-16 rex number, dash/space/separator and 2 digit size code.	
				try {
					salesNbr = productValidator.checkPosProdByRex(salesNbr);
				}
				catch (ValidationException e2){
					SwMessage result1 = new SwMessage();
					result1.setMessage(messageSource. getMessage("405", new Object[] {salesNbr, ""}, locale));
					result1.setCode("405");
					result1.setSeverity(Level.ERROR);
					result.add(result1);
				// Stop validation here if sales, rex/size or upc has not been validated.	
				// Cannot validate cdsProd sales number and size code if POS product not found.	
					return result;
				}
				catch (RuntimeException e2){
					logger.error(e2.getMessage());
					SherColorException se = sherColorExceptionBuilder.buildfrom(e2);
					throw se;
				} catch(Exception e2) {
					logger.error(e2.getMessage());
					throw e2;
				}
		// End final test.		
			}
			catch (RuntimeException e1){
				logger.error(e1.getMessage());
				SherColorException se = sherColorExceptionBuilder.buildfrom(e1);
				throw se;
			} catch(Exception e1) {
				logger.error(e1.getMessage());
				throw e1;
			};
		// End second test.	
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
		throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		};
		// End first test.

		// Check if the product is defined to Sher-Color by its sales number.
		try {
			productValidator.checkCdsProd(salesNbr);
		}
		catch (ValidationException e){
		// Add message number to list - Invalid product not defined to Sher-Color.
			SwMessage result2 = new SwMessage();
			result2.setMessage(messageSource. getMessage("406", new Object[] {salesNbr, ""}, locale));
			result2.setCode("406");
			result2.setSeverity(Level.WARN);
			result.add(result2);
			return result;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		// Check that has a valid product container by size code that Sher-Color can tint.
		try {
			productValidator.checkSizeCode(salesNbr);
		}
		catch (ValidationException e){
		// Add message number to list - Product size code is not defined to Sher-Color.
		// (valid 14 quart, 16 gallon and 20 five gallon)
			SwMessage result3 = new SwMessage();
			result3.setMessage(messageSource. getMessage("407", new Object[] {salesNbr, ""}, locale ));
			result3.setCode("407");
			result.add(result3);
			return result;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		SwMessage result4 = new SwMessage();
		
		result.add(result4);
		return result;
	}
	
	
	/* 08/17/2017 - Active Products Option */
	
	@Override
	public List<CdsProd> productAutocompleteActive(String searchCriteria) {
		List<CdsProd> list = null;
		CustWebActiveProds activeProds = null;

		try {
			list = cdsProdDao.listForAutocompleteProductActive(searchCriteria);
			//TODO Insert custwebactiveprods look-up here.
			// 2017/08/11 Active Product Check Logic
			if (!list.isEmpty()){
				// When list has objects, iterate through the list and remove items not found on the Active Prods tables.
				ListIterator<CdsProd> itListCdsProd = list.listIterator();
				while(itListCdsProd.hasNext()){
					// Active Prods table is CustWebActiveProds
					activeProds = activeProdsDao.getActiveProdBySalesNbr(itListCdsProd.next().getSalesNbr());
					if (activeProds == null)
						itListCdsProd.remove();
				}
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
			return list;
	}
	
	/* 08/17/2017 - Active Products Option */
	
	@Override
	public List<CdsProd> productAutocompleteBothActive(String searchCriteria) {
		List<CdsProd> list = null;
		List<PosProd> list2 = null;
		CdsProd newCdsProd;
		PosProd thisPosProd;

		try {
			list = cdsProdDao.listForAutocompleteProductActive(searchCriteria);
			if (list.isEmpty()) {
				list2 = posProdDao.listForAutocompleteActive(searchCriteria);
				if (!list2.isEmpty()) {
					//walk list 2, constructing cdsProd records from the PosProd records.
					for (int i = 0; i < list2.size(); i++) {
						thisPosProd = list2.get(i);
						newCdsProd = new CdsProd();
						newCdsProd.setSalesNbr(thisPosProd.getSalesNbr());
						newCdsProd.setPrepComment(thisPosProd.getProdNbr() + "-" + thisPosProd.getSzCd());
						newCdsProd.setQuality(thisPosProd.getDescr());
						list.add(newCdsProd);
					}
				}
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		//System.out.println("NEW Method Search Criteria -> " + searchCriteria + " List size -> " + list.size());
		return list;
	}
	

	@Override
	public List<CdsProd> productAutocomplete(String searchCriteria) {
		List<CdsProd> list = null;

		try {
			list = cdsProdDao.listForAutocompleteProduct(searchCriteria);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return list;
	}
	
	
	@Override
	public List<CdsProd> productAutocompleteBoth(String searchCriteria) {
		List<CdsProd> list = null;
		List<PosProd> list2 = null;
		CdsProd newCdsProd;
		PosProd thisPosProd;

		try {
			list = cdsProdDao.listForAutocompleteProduct(searchCriteria);
			if (list.isEmpty()) {
				list2 = posProdDao.listForAutocomplete(searchCriteria);
				if (!list2.isEmpty()) {
					//walk list 2, constructing cdsProd records from the PosProd records.
					for (int i = 0; i < list2.size(); i++) {
						thisPosProd = list2.get(i);
						newCdsProd = new CdsProd();
						newCdsProd.setSalesNbr(thisPosProd.getSalesNbr());
						newCdsProd.setPrepComment(thisPosProd.getProdNbr() + "-" + thisPosProd.getSzCd());
						newCdsProd.setQuality(thisPosProd.getDescr());
						list.add(newCdsProd);
					}
				}
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		//System.out.println("OLD Method Search Criteria -> " + searchCriteria + " List size -> " + list.size());
		return list;
	}
	
	@Override
	public List<PosProd> autocompleteProduct(String searchCriteria) {
		List<PosProd> list = null;
		

		try {
			list = posProdDao.listForAutocomplete(searchCriteria);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return list;
	}

	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ProductValidator getProductValidator() {
		return productValidator;
	}

	public void setProductValidator(ProductValidator productValidator) {
		this.productValidator = productValidator;
	}

	public CdsProdDao getCdsProdDao() {
		return cdsProdDao;
	}

	public void setCdsProdDao(CdsProdDao cdsProdDao) {
		this.cdsProdDao = cdsProdDao;
	}

	public PosProdDao getPosProdDao() {
		return posProdDao;
	}

	public void setPosProdDao(PosProdDao posProdDao) {
		this.posProdDao = posProdDao;
	}

	public SherColorExceptionBuilder getSherColorExceptionBuilder() {
		return sherColorExceptionBuilder;
	}

	public void setSherColorExceptionBuilder(SherColorExceptionBuilder sherColorExceptionBuilder) {
		this.sherColorExceptionBuilder = sherColorExceptionBuilder;
	}


	@Override
	public CdsProd readCdsProd(String salesNbr) {
		CdsProd returnRec;
		try {
			returnRec = cdsProdDao.read(salesNbr);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	@Override
	public String getSalesNbr(String upcOrSalesNbr){
		String retVal = null;
		PosProd posProd = null;
		
		upcOrSalesNbr = upcOrSalesNbr.toUpperCase();
		
		posProd = posProdDao.read(upcOrSalesNbr);
		
		if(posProd==null){
			posProd = posProdDao.readByUpc(upcOrSalesNbr);
			if(posProd==null) {
				// is this a prod number and size code with a space in between?
				if(upcOrSalesNbr.trim().indexOf(" ")>-1){
					// there is a space, try it
					String[] parsedBySpace = upcOrSalesNbr.trim().split(" ");
					if(parsedBySpace.length>1 && !parsedBySpace[0].trim().isEmpty() && !parsedBySpace[1].trim().isEmpty()){
						posProd = posProdDao.readByProdNbrSzCd(parsedBySpace[0], parsedBySpace[1]);
						if(posProd!=null) retVal = posProd.getSalesNbr(); //return value found by Prod Number and Size Code
					}
				} else {
					//PSCWEB-131 - Check if the item has a dash in it (e.g. A06W00053-16)
					if(upcOrSalesNbr.trim().indexOf("-")>-1){
						// there is a space, try it
						String[] parsedByDash = upcOrSalesNbr.trim().split("-");
						if(parsedByDash.length>1 && !parsedByDash[0].trim().isEmpty() && !parsedByDash[1].trim().isEmpty()){
							posProd = posProdDao.readByProdNbrSzCd(parsedByDash[0], parsedByDash[1]);
							if(posProd!=null) retVal = posProd.getSalesNbr(); //return value found by Prod Number and Size Code
						}
					}
				}
				// TODO still null?  maybe its prod number with no size code, default to size code 16?  Not now, lets gets some feedback first.
			} else {
				// return value found by UPC
				retVal = posProd.getSalesNbr();
			}
		} else {
			// return value found by Sales Number
			retVal = posProd.getSalesNbr();
		}
		
		return retVal;
	}
	
	@Override
	public CdsProdCharzd readCdsProdCharzd(String prodNbr, String clrntSysId) {
		CdsProdCharzd returnRec;
		try {
			returnRec = cdsProdCharzdDao.read(prodNbr, clrntSysId);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}

	public String getSizeText(String sizeCode) {
		String sizeText = "";
		
		if(!StringUtils.isEmpty(sizeCode)){
			switch (sizeCode) {
			case "01":
				sizeText = "11OZ";
				break;
			case "12":
				sizeText = "1/2PT";
				break;
			case "13":
				sizeText = "PINT";
				break;
			case "14":
				sizeText = "QUART";
				break;
			case "16":
				sizeText = "GALLON";
				break;
			case "17":
				sizeText = "2GAL";
				break;
			case "18":
				sizeText = "12OZ";
				break;
			case "19":
				sizeText = "4GL-KT";
				break;
			case "20":
				sizeText = "5GAL";
				break;
			case "21":
				sizeText = "16OZ";
				break;
			case "27":
				sizeText = "LG DRM";
				break;
			case "30":
				sizeText = "3GL-KT";
				break;
			case "32":
				sizeText = "3.5GAL";
				break;
			case "35":
				sizeText = "GL-KT";
				break;
			case "40":
				sizeText = "20L";
				break;
			case "85":
				sizeText = "200LTR";
				break;
			default:
				sizeText = "EACH";
				break;
			}
		}

		return sizeText;
	}

	public OeServiceProdDataSet getDsProdFromOracleBySalesNbr(String salesNbr){
		OeServiceProdDataSet dsProd = new OeServiceProdDataSet();
		
		PosProd posProd = posProdDao.read(salesNbr);
		if(posProd!=null){
			CdsProd cdsProd = cdsProdDao.read(salesNbr);
			List<CdsProdCharzd> cdsProdCharzdList = cdsProdCharzdDao.listForProdNbr(posProd.getProdNbr(), true);
			
			List<OeServiceSwProd> oeSwProdList = new ArrayList<OeServiceSwProd>();
			oeSwProdList.add(new OeServiceSwProd(posProd));
			dsProd.setSwProd(oeSwProdList);
			
			List<OeServiceCdsProd> oeCdsProdList = new ArrayList<OeServiceCdsProd>();
			OeServiceCdsProd oneDsCdsProd = new OeServiceCdsProd(cdsProd);
			oneDsCdsProd.setProdNbr(posProd.getProdNbr());
			oeCdsProdList.add(oneDsCdsProd);
			dsProd.setCdsProd(oeCdsProdList);
			
			if(cdsProdCharzdList.size()>0){
				// get records, convert to Oe format, add to master Oe Cds Prod Charzd List
				List<OeServiceCdsProdCharzd> oeCdsProdCharzdList = new ArrayList<OeServiceCdsProdCharzd>();
				for(CdsProdCharzd thisCharzd : cdsProdCharzdList){
					oeCdsProdCharzdList.add(new OeServiceCdsProdCharzd(thisCharzd));
				}
				dsProd.setCdsProdCharzd(oeCdsProdCharzdList);
			}
			
			List<OeServiceLargeObjectStorage> clobStorageList = new ArrayList<OeServiceLargeObjectStorage>();
			//dsProd.setObjStorage(clobStorageList);
			
		} else {
			dsProd = null;
		}
		
		return dsProd;
	}
	
	public OeServiceProdDataSet getDsProdFromOpenEdgeBySalesNbr(String salesNbr){
		OeServiceProdDataSet dsProd = null;
		URL initUrl;
		HttpURLConnection conn = null;

		try {
			//TODO finish and test this method...
			initUrl = new URL(oeBaseUrl.toString()+"formulationReq");
			conn = (HttpURLConnection) initUrl.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			String inputBody="";
			OutputStream os = conn.getOutputStream();
			os.write(inputBody.getBytes());
			os.flush();
			
			int responseCode = conn.getResponseCode();
			System.out.println("responsecode is " + responseCode);
			
			if(responseCode==200 || responseCode==201){
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = br.readLine()) != null){
					response.append(inputLine);
				}
				
				System.out.println("json response from create= " + response.toString());
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				GsonBuilder builder = new GsonBuilder();
				//Gson gson = builder.create();
				gson = builder.setDateFormat("yyyy-MM-dd").create();
				dsProd = gson.fromJson(response.toString(), OeServiceProdDataSet.class);
			} else {
				//TODO Error Handling
			}
			
			/* json response should be something like
			 */

		} catch (MalformedURLException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			SherColorException se = new SherColorException();
			// TODO setup exception codes/errors for this 
			throw se;
		} catch (ConnectException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			SherColorException se = new SherColorException();
			// TODO setup exception codes/errors for this 
			throw se;
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			SherColorException se = new SherColorException();
			// TODO setup exception codes/errors for this 
			throw se;
		} finally {
			if(conn!=null) conn.disconnect();
		}

		return dsProd;
	}
	
	public PosProd readPosProd(String salesNbr){
		PosProd returnRec;
		try {
			returnRec = posProdDao.read(salesNbr);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	public String getOeBaseUrl() {
		return oeBaseUrl;
	}

	public void setOeBaseUrl(String oeBaseUrl) {
		this.oeBaseUrl = oeBaseUrl;
	}

	public ProductFillInfo getProductFillInfo(String salesNbr, String clrntSysId){
		ProductFillInfo productFillInfo = new ProductFillInfo();
		
		// how much room in the can...
		CdsProd cdsProd = null;
		PosProd posProd = null;
		CdsProdCharzd cdsProdCharzd = null;
		
		cdsProd = cdsProdDao.read(salesNbr);
		if(cdsProd!=null){
			posProd = posProdDao.read(salesNbr);
			if(posProd!=null){
				cdsProdCharzd = cdsProdCharzdDao.read(posProd.getProdNbr(), clrntSysId);
				if(cdsProdCharzd==null) {
					// uh-oh, no charzd for this clrntSys, try to find something...
					List<CdsProdCharzd> charzdList = cdsProdCharzdDao.listForProdNbr(posProd.getProdNbr(), true);
					if(charzdList.size()> 0){
						// get the first one, they should all have the same fill levels
						cdsProdCharzd = charzdList.get(0);
					}
				}
				
				// we have a posProd, get sizeRatio
				List<CdsMiscCodes> miscCodes = cdsMiscCodesDao.listForType("SZRAT");
				for(CdsMiscCodes thisMisc : miscCodes){
					if(thisMisc.getMiscCode().endsWith(posProd.getSzCd())){
						Double tmpRatio = Double.valueOf(thisMisc.getMiscName().trim());
						if(!tmpRatio.isNaN() && !tmpRatio.isInfinite()) productFillInfo.setSizeRatio(tmpRatio.doubleValue());
					}
				}
			}
		}
		
		if(cdsProdCharzd!=null){
			// get the fill per gallon
			productFillInfo.setPerGallonMaxLoad(cdsProdCharzd.getMaxFill() - cdsProdCharzd.getFactoryFill());
			if(cdsProdCharzd.getOverFill()>0D){
				productFillInfo.setPerGallonMaxOverLoad(cdsProdCharzd.getOverFill() - cdsProdCharzd.getFactoryFill());
			} else {
				productFillInfo.setPerGallonMaxOverLoad(0D);
			}
			
			// apply size ratio for product load info
			if(productFillInfo.getSizeRatio()>0D) {
				productFillInfo.setProductMaxLoad(productFillInfo.getPerGallonMaxLoad() * productFillInfo.getSizeRatio());
				productFillInfo.setProductMaxOverLoad(productFillInfo.getPerGallonMaxOverLoad() * productFillInfo.getSizeRatio());
				productFillInfo.setEstimateMaxLoad(productFillInfo.getEstimatePerGallonMaxLoad() * productFillInfo.getSizeRatio());
			}
		}
		
		return productFillInfo;
	}
}
