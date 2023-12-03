package cliente;

public enum HttpMethod {
	GET;
	
	public static HttpMethod isMethod(String m) {
		for(HttpMethod sm : HttpMethod.values()) {
			if(sm.toString().equals(m)) {
				return sm;
			}
		}
		return null;
	}
}
