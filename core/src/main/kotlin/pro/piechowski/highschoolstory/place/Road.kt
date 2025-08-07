package pro.piechowski.highschoolstory.place

import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.map.Map

object Road : Place("Road", Map(AssetIdentifiers.Maps.Road, Map.Scrolling.Horizontal(200f)))
