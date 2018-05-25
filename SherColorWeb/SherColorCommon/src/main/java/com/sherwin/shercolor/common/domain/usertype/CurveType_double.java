package com.sherwin.shercolor.common.domain.usertype;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class CurveType_double implements UserType{
	// For java 6
	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object value) throws HibernateException, SQLException {
		Array sqlArray = resultSet.getArray(names[0]);
		double[] retval = null;
        if( resultSet.wasNull() ){
        	//return null;
        	return retval;
        	
        }

        //return sqlArray.getArray();
        BigDecimal[] bdarr = (BigDecimal[]) sqlArray.getArray();
		retval = new double[40];
		Arrays.fill(retval, 0D);
		for(int i=0;i<40;i++){
			retval[i]=bdarr[i].doubleValue();
		}

        return retval;
	}
	
	// For java 6
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
		if( null == value ){
        	Connection connection = sessionImplementor.getJdbcConnectionAccess().obtainConnection();
        	ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "CURVEARRAY", connection ); 
        	ARRAY array = new ARRAY( descriptor, connection, null);
			//preparedStatement.setNull(index, Types.ARRAY);
			preparedStatement.setObject(index, array);
		}
        else{
        	double[] data = (double[]) value;
        	
        	Object[] objData = new Object[data.length];
        	for(int i=0; i<data.length; i++){
        		objData[i] = new Double(data[i]);
        	}
        	
        	// This is a new connection obtained by the implementor. It is NOT the connection used by the
        	// the preparedStatement.
        	
        	// It MUST be released.
        	// The documentation in Hibernate is misleading.
        	Connection connection = sessionImplementor.getJdbcConnectionAccess().obtainConnection();
        	
        	ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "CURVEARRAY", connection ); 
        	ARRAY array = new ARRAY( descriptor, connection, objData);
        	
        	sessionImplementor.getJdbcConnectionAccess().releaseConnection(connection);
        	
        	preparedStatement.setArray(index, array);
        }
	}

	// For java 5
//	public Object nullSafeGet(ResultSet resultSet, String[] names, Object value) throws HibernateException, SQLException {
//		Array sqlArray = resultSet.getArray(names[0]);
//		
//        if( resultSet.wasNull() ){
//        	return null;
//        }
//
//        return sqlArray.getArray();
//	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object value) throws HibernateException, SQLException {
		return Arrays.asList(ArrayUtils.toObject(((ARRAY) resultSet.getObject(names[0])).getDoubleArray()));
	}

	// For java 5
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
		if( null == value ){
			ARRAY array = null;
			//preparedStatement.setNull(index, Types.ARRAY);
			preparedStatement.setObject(index, array);
		}
        else{
        	double[] data = (double[]) value;
        	
        	DelegatingConnection del = (DelegatingConnection) preparedStatement.getConnection();
        	
        	OracleConnection oracleConnection = (OracleConnection) del.getInnermostDelegate();
        	
        	Array array = oracleConnection.createARRAY("CURVEARRAY", data);
        	preparedStatement.setArray(index, array);
        }
	}
	
	
	
	public Object assemble(Serializable cached, Object owner)	throws HibernateException {
		return cached;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable)value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if( x == y){
			 return true;
		}
           
        if( null == x || null == y ){
        	return false;
        }
            
        Class<double[]> javaClass = returnedClass();
        
        if( ! javaClass.equals( x.getClass() ) || ! javaClass.equals( y.getClass() ) ){
        	return false;
        }
              
        return x.equals( y );
	}

	public int hashCode(Object obj) throws HibernateException {
		return obj.hashCode();
	}

	public boolean isMutable() {
		return true;
	}



	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public Class<double[]> returnedClass() {
		return double[].class;
	}

	public int[] sqlTypes() {
		return new int[] {Types.ARRAY};
	}




}
