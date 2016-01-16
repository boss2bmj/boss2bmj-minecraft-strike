//CrafterStrike

package shootergame;

import java.util.List;
import java.util.ArrayList;
import net.canarymod.Canary;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.player.BlockLeftClickHook;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Block;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.plugin.PluginListener;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.canarymod.hook.player.ChatHook;
import net.canarymod.api.entity.Arrow;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.commandsys.*;
import net.canarymod.logger.Logman;
import java.util.ArrayList;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.hook.player.PlayerRespawnedHook;
import net.canarymod.hook.player.BlockLeftClickHook;





public class ShooterGame extends EZPlugin implements PluginListener{


	private ArrayList<Block> blockList = new ArrayList<Block>();//this make a list of the block
	private ArrayList<Player> playerList1 = new ArrayList<Player>();//this make a list of the player team1 
	private ArrayList<Player> playerList2 = new ArrayList<Player>();//this make a list of the player team2
	private int score1  = 0;//This set score of 1 team
	private int score2 = 0;//This set score of 2 team
	private Location spawnPoint1 = new Location(-883,83,74);
	private Location spawnPoint2 = new Location(-842,84,69);
	private Location spwanPointsq = new Location(-866,81,77);
	private Location movetoemeraldBlock = new Location(-883,96,85);
	private Location movetodiamondBlock = new Location(-883,87,83);
	private Location movetoironBlock = new Location(-839,97,58);
	private Location movetogoldBlock= new Location(-838,88,61);




//-----------------------------------------------------------------------------------------------''
	private void giveTheitem(Player me){
		Item bow = getItem(ItemType.Bow);
    	Item  arrow = getItem(ItemType.Arrow);
    	Item woodsword  = getItem(ItemType.WoodSword );
    	arrow.setAmount(64);//give the max no of the arrow
    	me.giveItem(bow);//give the bo to player
    	me.giveItem(arrow);//give the arrow to player
    	me.giveItem(arrow);//give the arrow to player
    	me.giveItem(woodsword);//give the woodsword to player
	}


//-----------------------------------------------------------------------------------------------''
	private void deletePlayer(Player player){
		//here is the function that i want to delete the player before putting them in to the new list
		if (playerList1.contains(player)){
			playerList1.remove(player);
		}
		else if(playerList2.contains(player)){
			playerList2.remove(player);
		}
	}

//-----------------------------------------------------------------------------------------------''
	public void assignToteam(Player player, World world, String teamnumber){
		//this function will assign the player to teams that they want
		deletePlayer(player);//delete first if the name of the player is in the list
		if(teamnumber.equals ("1")) {
			//if the user type"1"
			player.teleportTo(spawnPoint1);//teleport to the landmark of team 1
			playerList1.add(player);//the player will add into the 1 list
			world.broadcastMessage("Welcome to the GREEN TEAM \nLet's kill them!!!!"+
				"\nIf you want to know the score press score in command");
		}
		else if(teamnumber.equals("2")){
			//if the user type "2"
			player.teleportTo(spawnPoint2);//teleport to the landmark of team 2
			playerList2.add(player); //the player will add into the 2 list
			world.broadcastMessage("Welcome to the WHITE TEAM \nLet's kill them!!!!"+
				"\nIf you want to know the score press score in command");
		}
	}

//-----------------------------------------------------------------------------------------------''
	//This will start when all the player connect to my server
	//it will bring the player to the location that i set
	@HookHandler
	public void onConnect(ConnectionHook event){
		//getPlayer() is a method
		//this is connection hook will start when player connect to my server
		Player me = event.getPlayer();
		World w = me.getWorld();
		w.broadcastMessage("WELCOME TO THE SHOOTER GAME!!! \n" + 
			"There are 2 teams which are GREEN and WHITE TEAM \n" +
			"If you want to choose GREEN Please press 1\n If you want to choose WHITE Please press 2"+
			"\nor if you do not want to press 1 or 2 you can press a button to choose a team!!");
		me.teleportTo(spwanPointsq);//This will teleport to the start point
		giveTheitem(me);//Here, i want to give a bow, arrow, and sword to the player
	}

//-----------------------------------------------------------------------------------------------''
	//Here i will make a chat hook that the player can choose the team
	//have to check first if player is in the list check wa contain in with list 
	@HookHandler
	public void onChat(ChatHook event){
		//This command will start when player choose team by using chat hook
		Player me = event.getPlayer();//set Player me = event.getPlayer()
		World w = me.getWorld(); //set World wo = me.getWorld
		String message = event.getMessage();//set String message = event.getMessage
		assignToteam(me,w,message);//this function will assign the player to team
	}

//-----------------------------------------------------------------------------------------------''
	//this will make an arrow to be more damage
	@HookHandler 
	public void onProjecttile(ProjectileHitHook event){
		//i use ProjectileHitHook to set a damage of the arroww
		if(event.getProjectile() instanceof Arrow){
			Arrow a = (Arrow)event.getProjectile();// i set Arrow a = (Arrow)event.getProjectile
			a.setDamage(1000);//set the damage of the arrow
		}
	}


//-----------------------------------------------------------------------------------------------''
	//here, i will set the score by count from the player who die 
	//i use PlayerDeathHook to set that hen player die the score will be increase 
	@HookHandler
	public void onDeath(PlayerDeathHook event){
		//if the player that die is on team 1
		if (playerList1.contains(event.getPlayer())){
			score2 ++;//team 2  +1point
			Canary.instance().getServer().broadcastMessage("Score team 2 ="+score2);
			//this line will print a score in the game
			// System.out.println("Score team 2 =" + score2);//this line is for debug
		}

		//else if the player that die is on team 2
		else if(playerList2.contains(event.getPlayer())) {
			// System.out.println("************************");//this line is for debug
			score1 ++;//team 1  +1point
			Canary.instance().getServer().broadcastMessage("Score team 1 ="+score1);//this line ill print a score in the game
			// System.out.println("Score team 1 =" + score1);//this line is for debug

		}

	}

//-----------------------------------------------------------------------------------------------''
	//This Hook is start when PlayerRespawnedHook
	//i will use this hook to set the place where player should spawn and also give the items
	@HookHandler
	public void onRespawned(PlayerRespawnedHook event){
		//start the hook 
		//if the playe die
		if (event.getPlayer() instanceof Player) {
			Player me = (Player)event.getPlayer();//i set Player me = (Player)event.getPlayer
		// System.out.println("give me weapon when player spawn");//this line is for debug
			giveTheitem(me);//give weapons the player

    	//Move player to coresponding spwan point and according to the teams
    		if ( playerList1.contains(me) ){
    			//team1
    			me.teleportTo(spawnPoint1);//teleporTo place of team 1
    		}
    		else if (playerList2.contains(me)){
    			//team 2
    			me.teleportTo(spawnPoint2);//teleporTo place of team 2
    		}
		}
	}

//-----------------------------------------------------------------------------------------------''
	//This Hook will make the player to move to top and bottom of the town
	@HookHandler
	public void onRightclick(BlockRightClickHook event){
		Player me = event.getPlayer();
		World w = me.getWorld();
		//This for the Green team
		if (event.getBlockClicked().getType() == BlockType.EmeraldBlock ){
			//This will start when user clikright to the block to go to the top of the town of the green team
			me.teleportTo(movetoemeraldBlock);
		}
		else if (event.getBlockClicked().getType() == BlockType.DiamondBlock){
			//This will start when user clikleft to the block to go down the town of the green team
			me.teleportTo(movetodiamondBlock);
		}
		//This for the white team
		else if(event.getBlockClicked().getType() == BlockType.IronBlock){
			//This will start when user clikright to the block to go to the top of the town of white team
			me.teleportTo(movetoironBlock);
		}
		else if(event.getBlockClicked().getType() == BlockType.GoldBlock){
			//This will start when user clikleft to the block to go down the town of the white team
			me.teleportTo(movetogoldBlock);
		} 
		//Choose Block if u want to choose team Green
		else if(event.getBlockClicked().getType() == BlockType.StoneButton ){
			assignToteam(me,w,"1");//this will go to assign to team first
			me.teleportTo(spawnPoint1);
		} 
		//Choose Block if u want to choose team White
		else if(event.getBlockClicked().getType() == BlockType.WoodenButton ){
			assignToteam(me,w,"2");//this will go to assign to team first
			me.teleportTo(spawnPoint2);
		} 
		
	}

//-----------------------------------------------------------------------------------------------''
//Player cannot destroy the box
	@HookHandler
	public void leftClicktoBox(BlockLeftClickHook event){
		event.setCanceled();// i set event to  canceled
	}

//-----------------------------------------------------------------------------------------------
	@Override
	public boolean enable(){
		Canary.hooks().registerListener(this,this);
		return super.enable();
	}
	//This hook is for seting the damage on fire to be very harm
	//i use DamageHook
	@HookHandler
  	public void onFire(DamageHook event){
    	event.setDamageDealt(1000); // i set event.setDamageDealt(1000)
  	}

//-----------------------------------------------------------------------------------------------
  //This is for command quitgame
  @Command(aliases = { "quitgame" },
            description = "quit a game",
            permissions = { "" },
            toolTip = "/quitgame")
    public void QuiteGame(MessageReceiver caller, String[] parameters) {
       	if (caller instanceof Player) {
      	Player me = (Player)caller;
      	World w = me.getWorld();
		w.broadcastMessage("THANK YOU FOR PLAYING THIS GAME!!! \n " +
			"see you again net time \n  If you want to play again just press 1 or 2"+
			"\nor just click the button to play againwwww");
        me.teleportTo(spwanPointsq);
        //System.out.println(blockList.size());
        //System.out.println("x from first ele of blocklist = " + blockList.get(0).getX());
    }
}


//-----------------------------------------------------------------------------------------------
//This is for reset a score 
//i ill use a command resets to reset a score in case of want to start a new game
@Command(aliases = { "resets" },
            description = "reset the score",
            permissions = { "" },
            toolTip = "/resets")
    public void ResetGame(MessageReceiver caller, String[] parameters) {
       	if (caller instanceof Player) {
       		Canary.instance().getServer().broadcastMessage("Score team 1 ="+score1);
       		Canary.instance().getServer().broadcastMessage("Score team 2 ="+score2);
      		score1 = 0;
      		score2 = 0;
      		Canary.instance().getServer().broadcastMessage("The Score will be reseted");
      		Canary.instance().getServer().broadcastMessage("Score team 1 ="+score1);
      		Canary.instance().getServer().broadcastMessage("Score team 2 ="+score2);
        //System.out.println(blockList.size());
        //System.out.println("x from first ele of blocklist = " + blockList.get(0).getX());
    }
}


//This command will use to know the score
@Command(aliases = { "score" },
            description = "the score",
            permissions = { "" },
            toolTip = "/score")
    public void ScoreGame(MessageReceiver caller, String[] parameters) {
       	if (caller instanceof Player) {
       		Canary.instance().getServer().broadcastMessage("Score team 1 ="+score1);
       		Canary.instance().getServer().broadcastMessage("Score team 2 ="+score2);
		}
       		
    }
}






