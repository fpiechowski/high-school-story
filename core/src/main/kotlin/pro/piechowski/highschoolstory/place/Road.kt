package pro.piechowski.highschoolstory.place

import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.map.Map
import pro.piechowski.highschoolstory.physics.mps

object Road : Place("Road", Map(AssetIdentifiers.Maps.Road, Map.Scrolling.Horizontal((10f).mps)))
