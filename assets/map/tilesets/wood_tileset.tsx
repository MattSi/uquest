<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.9" tiledversion="1.9.1" name="wood_tileset" tilewidth="32" tileheight="32" tilecount="256" columns="16">
 <image source="../sources/wood_tileset.png" width="512" height="512"/>
 <tile id="0" probability="0.2"/>
 <tile id="1" probability="0.01"/>
 <tile id="16" probability="0.01"/>
 <tile id="17" probability="0.01"/>
 <tile id="32" probability="0.01"/>
 <tile id="33" probability="0.2"/>
 <wangsets>
  <wangset name="WoodTileSet" type="corner" tile="0">
   <wangcolor name="Grass" color="#ff0000" tile="0" probability="1"/>
   <wangcolor name="Road" color="#00ff00" tile="19" probability="1"/>
   <wangtile tileid="0" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="1" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="2" wangid="0,1,0,2,0,1,0,1"/>
   <wangtile tileid="3" wangid="0,1,0,2,0,2,0,1"/>
   <wangtile tileid="4" wangid="0,1,0,1,0,2,0,1"/>
   <wangtile tileid="16" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="17" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="18" wangid="0,2,0,2,0,1,0,1"/>
   <wangtile tileid="19" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="20" wangid="0,1,0,1,0,2,0,2"/>
   <wangtile tileid="32" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="33" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="34" wangid="0,2,0,1,0,1,0,1"/>
   <wangtile tileid="35" wangid="0,2,0,1,0,1,0,2"/>
   <wangtile tileid="36" wangid="0,1,0,1,0,1,0,2"/>
   <wangtile tileid="48" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="49" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="50" wangid="0,1,0,1,0,1,0,1"/>
  </wangset>
 </wangsets>
</tileset>
