package xyz.bedition;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.Player;
import cn.nukkit.Server;
import java.lang.String;
import java.lang.Double;
import java.util.Map;
import java.util.LinkedHashMap;
import me.onebone.economyapi.EconomyAPI;

public class TaxTask extends PluginTask<SpecificTax>{
	public TaxTask(SpecificTax plugin){
		super(plugin);
	}
	@Override
	public void onRun(int tick){
		Map<String,Double> map=new LinkedHashMap<String,Double>(EconomyAPI.getInstance().getAllMoney());
		Server server=getOwner().getServer();
		for(String player:map.keySet()){
			if(server.getOnlinePlayers().containsValue(server.getPlayer(player))) return;
			getOwner().collect(player);
		}
	}
}