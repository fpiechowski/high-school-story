package pro.piechowski.highschoolstory.map

import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.map.RepeatingTiledMapAdapter

class Road : RepeatingTiledMapAdapter("Road", get<Assets>().maps.road, repeatX = true)
