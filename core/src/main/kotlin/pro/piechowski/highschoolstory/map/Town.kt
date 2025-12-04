package pro.piechowski.highschoolstory.map

import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.map.TiledMapAdapter
import pro.piechowski.kge.physics.METERS_PER_PIXEL
import pro.piechowski.kge.physics.PIXELS_PER_METER

class Town : TiledMapAdapter("Town", get<Assets>().maps.town, METERS_PER_PIXEL)
