<div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">

This mod is for the [Fabric mod loader](https://www.fabricmc.net/) and <a href="https://www.curseforge.com/minecraft/mc-mods/leaves-us-in-peace/files"><img style="vertical-align:middle" src="https://cf.way2muchnoise.eu/versions/Minecraft_leaves-us-in-peace_all.svg" alt="Minecraft Versions"></a>

<a href="https://modrinth.com/mod/fabric-api/versions"><img src="https://i.imgur.com/Ol1Tcf8.png" alt="Requires Fabric API" width="300"></a>

Supports [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files) and [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu/files) for configuration, but neither is required.

Download the mod on [Modrinth](https://modrinth.com/mod/leaves-us-in-peace) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/leaves-us-in-peace)!

Like this mod?
<a href="https://coindrop.to/supersaiyansubtlety"><img height="20" style="vertical-align:middle" src="https://coindrop.to/embed-button.png" alt="Coindrop.to me"></a> or
<a href="https://ko-fi.com/supersaiyansubtlety"><img height="20" style="vertical-align:middle" src="https://i.ibb.co/4gwRR8L/p.png" alt="Buy me a coffee"></a>

<a href="https://github.com/supersaiyansubtlety/leaves_us_in_peace/blob/master/LICENSE"><img style="vertical-align:middle" src="https://img.shields.io/badge/license-CC0-green" alt="License CC0 1.0"></a>
<a href="https://www.curseforge.com/minecraft/mc-mods/leaves-us-in-peace/files"><img style="vertical-align:middle" src="https://cf.way2muchnoise.eu/full_leaves-us-in-peace_downloads.svg" alt="CurseForge Downloads"></a>

</div>

This mod aims to make leaf decay less frustrating.

It started out as a fork of [Tfarcenim's](https://github.com/Tfarcenim) [LeafMeAlone](https://github.com/Tfarcenim/LeafMeAlone), but then I overhauled it to the point of it being an almost entirely different mod.

It does 3 (+1) things to change how leaves decay, all configurable:
1) Leaves will ignore leaves of a different type when determining whether to decay or not. By default, this will only 
   match identical leaves (like in LeafMeAlone), but if a tag for leaves is found, all leaves in that tag will match. 
   This is how the mod handles azaleas' two types of leaves. Mods (or mod users) can add compat for trees with multiple 
   types of leaves by just adding a tag. Details on the wiki. 
2) Leaves will ignore logs from different tree types when determining whether to decay or not. This will look for a tag 
   by the name of the log and check if it contains the leaves doing the check. If no tag is found, any log will match 
   instead (like in vanilla).
3) Leaves will decay much faster. This is basically like the fast leaf decay mods you've likely seen before. It's 
   highly configurable, and uses an algorithm that can check for diagonal leaves, in addition to the usual check for 
   adjacent leaves, so it's less likely to leave a stray leaf or two floating.
- 3 + 1. This isn't convenient or less frustrating, but leaves can optionally make a block breaking sound and create 
   particles when they decay. 

<details>

<summary>Configuration</summary>

Configuration requires [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files), 
and configuring the mod in-game requires [Mod Menu](https://modrinth.com/mod/modmenu/versions).

##### Options:

- Match leaves types: Leaves ignore leaves of other types when determining whether to decay or not
- Match logs to leaves: Leaves ignore logs of other tree types when determining whether to decay or not
- Ignore persistent leaves: Leaves ignore persistent leaves (placed by player) when determining whether to decay or not
- Accelerate leaves decay: Make leaves decay much faster
- Decay delay: Random delay between leaves decaying and updating nearby leaves. Has no effect if 'Accelerate leaves decay' is 'No'.
  - Minimum: Minimum random delay. Set minimum equal to maximum to eliminate randomness.
  - Maximum: Maximum random delay
- Update diagonal leaves: When leaves decay, update leaves diagonal to them in addition to those adjacent to them
- Do decaying leaves effect: create particles and sounds when leaves decay
- Download translation updates: Download translations from Crowdin when the game launches

</details>

This mod is only required server-side: clients don't need it to connect to servers that have it. It also works in singleplayer.

<details>

<summary>Credits</summary>

- [Tfarcenim's](https://github.com/Tfarcenim) for making LeafMeAlone.

- [glisco](https://github.com/glisco03) for making [Isometric Renders](https://github.com/glisco03/isometric-renders), which I used to create the mod icon.

</details>

This mod is only for Fabric and I won't be porting it to Forge. The license is [CC0 1.0](https://github.com/supersaiyansubtlety/leaves_us_in_peace/blob/master/LICENSE), however, so anyone else is free to port it.

I'd appreciate links back to this page if you port or otherwise modify this project, but links aren't required. 
