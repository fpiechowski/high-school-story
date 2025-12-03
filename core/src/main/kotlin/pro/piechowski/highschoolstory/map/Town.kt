package pro.piechowski.highschoolstory.map

import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.map.TiledMapAdapter

class Town : TiledMapAdapter("Town", get<Assets>().maps.town)
