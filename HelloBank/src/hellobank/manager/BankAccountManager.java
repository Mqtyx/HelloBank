package hellobank.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import hellobank.data.BankAccount;
import hellobank.main.Main;
import hellobank.utils.Utils;

public class BankAccountManager
{
	public static List<BankAccount> accounts = Lists.newArrayList();
	private File accountsFolder;
	private Map<BankAccount, File> accountToFile = Maps.newHashMap();
	
	public void loadAccounts()
	{
		for (File accountFile : getAccountsFolder().listFiles())
		{
			if (!accountFile.isDirectory())
			{
				if (!accountFile.getName().endsWith(".json"))
				{
					continue;
				}
				try(Reader reader = new FileReader(accountFile))
				{
					BankAccount data = Utils.GSON.fromJson(reader, BankAccount.class);
					accountToFile .put(data, accountFile);
					accounts.add(data);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private File getAccountsFolder()
	{
		if(accountsFolder == null)
		{
			accountsFolder = new File(Main.INSTANCE.getDataFolder(), "accounts");
		}
		
		if(!accountsFolder.exists())
		{
			accountsFolder.mkdirs();
		}
		return accountsFolder;
	}

	public BankAccount getAccountFromUUID(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				return e;
			}
		}
		return null;
	}
	
	public boolean deleteAccount(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				accounts.remove(e);
				if(accountToFile.containsKey(e))
				{
					File accountFile = accountToFile.get(e);
					if(accountFile.exists()) accountFile.delete();
				}
				return true;
			}
		}
		return false;
	}

	public void addAccount(BankAccount bankAccount)
	{
		accounts.add(bankAccount);
		save(bankAccount);
	}

	public void save(BankAccount bankAccount)
	{
		File file = accountToFile.get(bankAccount);
		if(file == null)
		{
			file = new File(getAccountsFolder(), bankAccount.getUUID().toString() + ".json");
			accountToFile.put(bankAccount, file);
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if (bankAccount != null)
		{
			try (Writer writer = new FileWriter(file))
			{
				Utils.GSON.toJson(bankAccount, writer);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void saveAccounts()
	{
		for(BankAccount account : accounts)
		{
			save(account);
		}
	}
}
