package xyz.bedition;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.Command;
import cn.nukkit.event.EventHandler;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import java.util.LinkedHashMap;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import me.onebone.economyapi.EconomyAPI;

public class SpecificTax extends PluginBase implements Listener{
	public LinkedHashMap<String,Object> collected;
	@Override
	public void onEnable(){
		collected=new LinkedHashMap<String,Object>(getConfig().getAll());
		getServer().getPluginManager().registerEvents(this,this);
		getServer().getScheduler().scheduleRepeatingTask(new TaxTask(this),20*60*30);
	}
	@Override
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(args.length<1){
				player.sendMessage("§e당신이 낸 세금 : "+Double.parseDouble(collected.get(player.getName()).toString()));
				return true;
			}
			if(!collected.containsKey(args[0].toLowerCase())){
				sender.sendMessage("§e존재하지 않는 플레이어 입니다.");
				return false;
			}
			sender.sendMessage(args[0]+" : "+Double.parseDouble(collected.get(args[0].toLowerCase()).toString()));
			return true;
		}
		sender.sendMessage("총 모인 세금 : "+collected.values().stream().mapToDouble(e->Double.parseDouble(e.toString())).sum());
		return true;
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent event){
		Player player=event.getEntity();
		player.sendMessage("세금이 징수되었습니다. "+collect(player)+"$");
	}
	
	
	protected double collect(Player player){
		return collect(player.getName());
	}
	protected double collect(String name){
		name=name.toLowerCase();
		if(getServer().isOp(name)) return 0;
		double money=EconomyAPI.getInstance().myMoney(name);
		if(money<10000) return 0;
		double collecting=money*1/1000;
		if(collecting>=money||money<1000) return 0;
		EconomyAPI.getInstance().reduceMoney(name,collecting);
		if(!collected.containsKey(name)){
			collected.put(name,0);
		}
		collected.put(name,Double.parseDouble(collected.get(name).toString())+collecting);
		getConfig().setAll(collected);
		getConfig().save();
		return collecting;
	}
}