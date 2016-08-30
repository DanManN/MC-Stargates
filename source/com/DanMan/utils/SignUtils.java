/*    */ package hauptklassen;
/*    */ 
/*    */ import org.bukkit.block.Block;
/*    */ 
/*    */ public final class SignUtils
/*    */ {
/*    */   public static org.bukkit.block.BlockFace signFacing(org.bukkit.block.Sign sign)
/*    */   {
/*  9 */     org.bukkit.material.Sign signData = (org.bukkit.material.Sign)sign.getData();
/* 10 */     return signData.getFacing();
/*    */   }
/*    */   
/*    */   public static org.bukkit.block.Sign signFromBlock(Block block) {
/* 14 */     return (org.bukkit.block.Sign)block.getState();
/*    */   }
/*    */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/SignUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */