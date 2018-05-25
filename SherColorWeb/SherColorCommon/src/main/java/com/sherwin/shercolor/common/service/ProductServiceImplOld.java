package com.sherwin.shercolor.common.service;
/*package com.sherwin.shercolor.common.service;

import java.util.List;

import javax.validation.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	CdsProdDao cdsProdDao;

	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Override
	public List<String> validateProduct(String salesNbr) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> productAutocomplete(String searchCriteria) {
		List<String> list = null;
		

		try {
			list = cdsProdDao.listForAutocompleteProduct(searchCriteria);
		}
		catch (RuntimeException e){
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		}
		
		return list;
	}
	
	@Override
	public List<PosProd> autocompleteProduct(String searchCriteria) {
		List<PosProd> list = null;
		

		try {
			list = posProdDao.listForAutocomplete(searchCriteria);
		}
		catch (RuntimeException e){
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		}
		
		return list;
	}

}
*/