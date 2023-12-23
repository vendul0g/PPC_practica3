package messages;

public enum ControlMessageType {
	//Enumerado
	SET_TIME_REFRESH, GET_STATISTICS, INVALID, ACK, NACK, HELP, VERBOSE, NOT_VERBOSE,
	CONTROL_MODE, BROADCAST_MODE, CHANGE_UNIT, DISABLE, ENABLE, SHOW_LAST_ENTRY;

	//Funcionalidad
	public static ControlMessage getTypeCommandLine(String message) {
		String line[] = message.split(" ");
		ControlMessage m; 
		int p, v;
		String mode;
		
		switch(line[0]) {
			case "setrefresh":
				if (line.length != 3)
					return new ControlMessage(INVALID);
				
				try {
					p = Integer.valueOf(line[1]);
					v = Integer.valueOf(line[2]);
					
					m = new SetRefreshMessage(SET_TIME_REFRESH, p, v);
					return m;

				}catch(NumberFormatException e) {
					return new ControlMessage(INVALID);
				}
			
			case "help":
				return new ControlMessage(HELP);
			
			case "verbose":
				return new ControlMessage(VERBOSE);
				
			case "notverbose":
				return new ControlMessage(NOT_VERBOSE);
				
			case "statistic":
				return new ControlMessage(GET_STATISTICS);
				
			case "controlmode":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				mode = line[1];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CONTROL_MODE, mode);
			
			case "broadcastmode":
				if(line.length != 3)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				mode = line[2];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(BROADCAST_MODE, p, mode);
				
			case "changeunit":
				if(line.length != 3)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				mode = line[2];
				if(!mode.equals("celsius") && !mode.equals("farenheit"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CHANGE_UNIT, p, mode);
			
			case "disable":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				return new ControlMessage(DISABLE, p);
			
			case "enable":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				return new ControlMessage(ENABLE, p);
				
			case "showlastentry":
				return new ControlMessage(SHOW_LAST_ENTRY);
				
			default:
				return new ControlMessage(INVALID);
		}
	}
	
	public static ControlMessage getTypeURL(String s) {
		int v;
		String m;
		
		try {
			int idServer = Integer.valueOf(s.split("/")[1].split("\\?")[0]);
			String command = s.split("\\?")[1].split("=")[0];
			
			switch(command) {
			case "disable":
				return new ControlMessage(DISABLE, idServer);
			
			case "enable":
				return new ControlMessage(ENABLE, idServer);
			
			case "setrefresh":
				v = Integer.valueOf(s.split("=")[1]);
				return new SetRefreshMessage(SET_TIME_REFRESH, idServer, v);
				
			case "controlmode":
				m = s.split("=")[1];
				return new SetModeMessage(CONTROL_MODE, m);
				
			case "broadcastmode":
				m = s.split("=")[1];
				return new SetModeMessage(BROADCAST_MODE, idServer, m);
				
			case "showlastentry":
				return new ControlMessage(SHOW_LAST_ENTRY);
			default:
				return new ControlMessage(INVALID);
			}
		}catch(Exception e) {
			return new ControlMessage(INVALID);
		}
	}
	
	public static ControlMessage getTypeApiRest(String message) {
		String c;
		String line[];
		try{
			String aux[] = message.split("\\?"); 
			c = aux[0];
			line = aux;
		}catch(Exception e) {
			line = null;
			c = message;
		}
		String params[];
		ControlMessage m; 
		int p, v;
		String mode;
		switch(c) {
			case "setrefresh":
				params = line[1].split("&");
				
				//Comprobación de errores
				if (params.length != 2 
						|| !params[0].split("=")[0].equals("id_server") 
						|| !params[1].split("=")[0].equals("time"))
					return new ControlMessage(INVALID);
				
				try {
					p = Integer.valueOf(params[0].split("=")[1]);
					v = Integer.valueOf(params[1].split("=")[1]);
					
					m = new SetRefreshMessage(SET_TIME_REFRESH, p, v);
					return m;

				}catch(NumberFormatException e) {
					return new ControlMessage(INVALID);
				}
			
			case "help":
				return new ControlMessage(HELP);
			
			case "verbose":
				return new ControlMessage(VERBOSE);
				
			case "notverbose":
				return new ControlMessage(NOT_VERBOSE);
				
			case "statistic":
				return new ControlMessage(GET_STATISTICS);
				
			case "controlmode":
				//Comprobación de errores
				if (line.length != 2 
						|| !line[1].split("=")[0].equals("mode")) 
					return new ControlMessage(INVALID);
				
				mode = line[1].split("=")[1];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CONTROL_MODE, mode);
			
			case "broadcastmode":
				params = line[1].split("&");	
				
				//Comprobación de errores
				if (params.length != 2 
						|| !params[0].split("=")[0].equals("id_server") 
						|| !params[1].split("=")[0].equals("mode"))
					return new ControlMessage(INVALID);
				
				p = Integer.valueOf(params[0].split("=")[1]);
				mode = params[1].split("=")[1];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(BROADCAST_MODE, p, mode);
				
			case "changeunit":
				params = line[1].split("&");
				
				//Comprobación de errores
				if (params.length != 2 
						|| !params[0].split("=")[0].equals("id_server") 
						|| !params[1].split("=")[0].equals("unit"))
					return new ControlMessage(INVALID);
				
				p = Integer.valueOf(params[0].split("=")[1]);
				mode = params[1].split("=")[1];
				if(!mode.equals("celsius") && !mode.equals("farenheit"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CHANGE_UNIT, p, mode);
			
			case "disable":
				System.out.println(line.length);
				//Comprobación de errores
				if (line.length != 2 
						|| !line[1].split("=")[0].equals("id_server")) { 
					System.out.println("ll");
					return new ControlMessage(INVALID);
				}
				p = Integer.valueOf(line[1].split("=")[1]);
				return new ControlMessage(DISABLE, p);
			
			case "enable":
				//Comprobación de errores
				if (line.length != 2 
						|| !line[1].split("=")[0].equals("id_server")) 
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1].split("=")[1]);
				return new ControlMessage(ENABLE, p);
				
			case "showlastentry":
				return new ControlMessage(SHOW_LAST_ENTRY);
				
			default:
				return new ControlMessage(INVALID);
		}
	}
	
	public static ControlMessageType getByName(String s) {
		switch(s) {
		case "SET_TIME_REFRESH":
		    return SET_TIME_REFRESH;
		case "GET_STATISTICS":
		    return GET_STATISTICS;
		case "ACK":
		    return ACK;
		case "NACK":
			return NACK;
		case "HELP":
		    return HELP;
		case "VERBOSE":
		    return VERBOSE;
		case "NOT_VERBOSE":
		    return NOT_VERBOSE;
		case "CONTROL_MODE":
		    return CONTROL_MODE;
		case "BROADCAST_MODE":
		    return BROADCAST_MODE;
		case "CHANGE_UNIT":
		    return CHANGE_UNIT;
		case "DISABLE":
		    return DISABLE;
		case "ENABLE":
		    return ENABLE;
		default:
			return INVALID;
		}
	}
}
