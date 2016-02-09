package ecoRe.recyclingSystem;

public class Administrator {
	private final String username;
	private final String password;

	public Administrator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String toString() {
		return "Adminisrator Detail[Username = " + username + "]";
	}
}
