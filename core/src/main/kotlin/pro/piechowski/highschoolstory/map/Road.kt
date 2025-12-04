package pro.piechowski.highschoolstory.map

import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.map.RepeatingTiledMapAdapter
import pro.piechowski.kge.physics.METERS_PER_PIXEL
import pro.piechowski.kge.physics.PIXELS_PER_METER

class Road : RepeatingTiledMapAdapter("Road", get<Assets>().maps.road, METERS_PER_PIXEL, repeatX = true)
