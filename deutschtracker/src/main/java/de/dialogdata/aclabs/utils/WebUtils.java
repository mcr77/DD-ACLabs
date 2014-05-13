package de.dialogdata.aclabs.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class WebUtils {

	@SuppressWarnings("unchecked")
	public static <T> T findBean(String beanName, FacesContext context) {
	    ExternalContext externalContext = context.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Object object = sessionMap.get(beanName);
		return (T) object;
	}
	
}
