package hellobank.data;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

public class BankAccount {
	private UUID uuid;
	private String acc;
	private String password;
	public static List<BankAccount> accounts = Lists.newArrayList();
	
	public BankAccount(UUID uuid, String acc, String password) {
		this.uuid = uuid;
		this.acc = acc;
		this.password = password;
		accounts.add(this);
	}
	
	public static boolean isAccount(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean deleteAccount(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				accounts.remove(e);
				return true;
			}
		}
		return false;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUId(UUID uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return acc;
	}

	public void setAccount(String acc) {
		this.acc = acc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static boolean isTaken(String acc) {
		for (BankAccount e : accounts) {
			if (e.getAccount().equals(acc)) {
				return true;
			}
		}
		return false;
	}
}
