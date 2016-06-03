//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.GlobalMapLayer.js
// 功能：			以世界地图为数据的图层。
// 最后修改时间：	2008-1-22
//========================================================================== 

Type.registerNamespace('SuperMap');
SuperMap.GlobalMapLayer = function(container, param, map) {
	/// <summary>针对全球地图的显示相关的图层，全球地图使用墨卡托投影方式形成平面地图，然后将平面地图以格网形式分块显示。
	/// </summary>
	/// <param name="container" type="Object">显示客户端呈现图层的图层容器，即DOM对象，如div，img。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该客户端呈现图层中的GIS信息，如地图图片请求地址、分辨率等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	SuperMap.GlobalMapLayer.initializeBase(this);
	if(param.mapHandler) {
		SuperMap.GlobalMapLayer.prototype._imageUrls = new Array();
		SuperMap.GlobalMapLayer.prototype._imageUrls[0] = param.mapHandler;
	}
	
	if(!this._resolutions) {
		this._resolutions = [1.40625,0.703125,0.3515625,0.17578125,0.087890625,0.0439453125,0.02197265625,0.010986328125,0.0054931640625,0.00274658203125,0.001373291015625,0.0006866455078125,0.00034332275390625,0.000171661376953125,0.0000858306884765625,0.00004291534423828125];
	}
	this._map = map;
	// 最大放大级别默认为17
	this._maxZoomLevel = 17;
	this._initializeProjection(this._resolutions.length);
	this._enableImageBuffer = true;
	this._maxImageBufferCount = 256;
	this._bufferSize = 1;
	this._projection = new SuperMap.MercatorProjection(this._maxZoomLevel);
};
SuperMap.GlobalMapLayer.prototype = {
	_imageUrls : new Array(),
	_initializeProjection:function(zoomLevels) {
		// 目前暂时先写固定值，等以后变化了再修改
		var size = 256;
		var tileLat = 170.0;
		var tileLon = 360.0;
		this._tileLengths = new Array();
		//this._tileLats = new Array();
		//this._tileLons = new Array();
    	for(var d=0; d<zoomLevels; d++){
	        this._tileLengths.push(size);
	      //  this._tileLats.push(tileLat);
	        //this._tileLons.push(tileLon);
	        //tileLat/=2;
	        //tileLon/=2;
	        size*=2;
    	}
	},
	latLngToPixel:function(latLng, zoomLevel){
		/// <summary>将经纬度转换成地图像素坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		if(!zoomLevel) {
	        var mapScale = this._map.get_mapScale();
	        zoomLevel = this._resolveZoomLevel(mapScale)
	    }
	    // (0,0)点坐标
	    var originCenter = new SuperMap.Point2D(0, 0); 
		var originPixel = this._map.mapCoordToPixel(originCenter, this._map.get_mapScale());
	    var mapLength = this._tileLengths[zoomLevel];
	    originPixel.x = originPixel.x - mapLength/2;
	    originPixel.y = originPixel.y - mapLength/2;
	    var position = this._projection.latLngToPixel(point, zoomLevel);
        
		return new SuperMap.Point(position.x + originPixel.x , position.y + originPixel.y);
	},
	
	pixelToLatLng:function(point, zoomLevel){
		/// <summary>将地图像素坐标转换成经纬度坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		if(!zoomLevel) {
	        var mapScale = this._map.get_mapScale();
	        zoomLevel = this._resolveZoomLevel(mapScale)
	    }
	    var originCenter = new SuperMap.Point2D(0, 0); 
		var originPixel = this._map.mapCoordToPixel(originCenter, this._map.get_mapScale());
	    var mapLength = this._tileLengths[zoomLevel];
	    originPixel.x = originPixel.x - mapLength/2;
	    originPixel.y = originPixel.y - mapLength/2
	    point.x -= originPixel.x;
	    point.y -= originPixel.y;
		
	    return this._projection.pixelToLatLng(point, zoomLevel);
	},
	_tileCheckRange:function(tile, zoom, tileSize){
		// 检查tile是否在显示范围内
		if(zoom > this._maxZoomLevel || zoom < 0) {
		    return false;
		}
		
		var tileLength = this._tileLengths[zoom];
		if(!tileLength) {
		    return false;
		}
    	if(tile.y<0||tile.y*tileSize>=tileLength){
	        return false
	    }
	    if(tile.x<0||tile.x*tileSize>=tileLength){
	      var e= Math.ceil(tileLength/tileSize);
	      tile.x=tile.x%e;
	      if(tile.x<0){
	        tile.x+=e;
	      }
	    }
		
    	return true
	},
	_resolveZoomLevel:function(mapScale){
		// 将分辨率解析成对应的放大级别
		var zoom = -1;
		if(mapScale >= this._map._params.mapScales[0]){
			zoom = 0;
		} else if(mapScale < this._map._params.mapScales[this._maxZoomLevel-1]){
			zoom = this._maxZoomLevel;
		}
		for(var i = 0; i < this._map._params.mapScales.length; i++) {
			if((mapScale * 0.7 <= this._map._params.mapScales[i]) && (mapScale * 1.3 > this._map._params.mapScales[i])) {
				zoom = i;
				break;
			}
		}
		this._zoomLevel = zoom;
		
		return zoom;
	},
	dispose:function(){
	/// <summary>释放所占用的资源。
	/// </summary>
		if(this._tiles){
			while(this._tiles.length > 0){
				var tileRow = this._tiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile._image){
					    if(tile._image.parentNode != null) {
					        tile._image.parentNode.removeChild(tile._image);
					    }
					}
					if(tile._tempImage){
					    if(tile._tempImage.parentNode != null) {
					        tile_tempImage.parentNode.removeChild(tile._tempImage);
					    }
					}
					if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
					tile = null;
				}
				tileRow = null;
			}
			this._tiles = null;
		}
		if(this._oldTiles){
			while(this._oldTiles.length > 0){
				var tileRow = this._oldTiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile._image){
					    if(tile._image.parentNode != null) {
					        tile._image.parentNode.removeChild(tile._image);
					    }
					}
					if(tile._tempImage){
					    if(tile._tempImage.parentNode != null) {
					        ttile._tempImage.parentNode.removeChild(tile._tempImage);
					    }
					}
					if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
					tile = null;
				}
				tileRow = null;
			}
			this._oldTiles = null;
		}
		if(this._preTiles){
			while(this._preTiles.length > 0){
				var tile = this._preTiles.pop();
				if(tile._image){
				    if(tile._image.parentNode != null) {
				        tile._image.parentNode.removeChild(tile._image);
				    }
				}
				if(tile._tempImage){
				    if(tile._tempImage.parentNode != null) {
				        tile._tempImage.parentNode.removeChild(tile._tempImage);
				    }
				}
				if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
				tile = null;
			}
			this._preTiles = null;
		}
		this._param = null;
		if(this._container){
		    if(this._container.parentNode != null){
		        this._container.parentNode.removeChild(this._container);
		    }
		}
		this._container = null;
		this._map = null;
	},
	getTileUrl:function(x, y, zoomLevel){
		/// <summary>获取单个格网图片的Url地址。&lt;br&gt;
		/// 在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
		/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
		/// </summary>
		/// <param name="x" type="Number">格网图片所在的x（横向）索引值。</param>
		/// <param name="y" type="Number">格网图片所在的y（纵向）索引值。</param>
		/// /// <param name="zoomLevel" type="Number">地图放大级别。</param>
		var url = this._imageUrls[0] + "?mapName=" + this._map.get_mapName() + "&x=" + x + "&y=" + y + "&imageFormat=png&width=256&height=256&mapScale=" + 1.0 + "&zoom=" + (this._maxZoomLevel -zoomLevel) + "&layersKey=" + this._map._params.layersKey + "&method=getglobaltiledimage";
		
		return url;
	},
	_createTile:function(position, gridPx, ratio){
		var tile = new SuperMap.UI.MapTile(this._container);
		tile._id = gridPx.x + "," + gridPx.y;
		//tile._debug=true;
		var url = "../../images/supGis/spacer.gif";
		var xIndex = gridPx.x;
		var yIndex = gridPx.y;
		if(this._tileCheckRange(gridPx, this._zoomLevel, 256)) {
			url = this.getTileUrl(gridPx.x, gridPx.y, this._zoomLevel);
		}
		//Debug("tile position:" + position.x + "," + position.y + " " + gridPx.x + "," +gridPx.y + " url:" + url + " zoom:" + this._zoomLevel);
		//Debug(this._zoomLevel + "," + url);
		var mapScale = this._map.get_mapScale();
		var tileSize = Math.round(ratio * 256);
		tile._tileSize = tileSize;
		
		var px = position.x;
		var py = position.y;
		try{
			var imageId = xIndex + "," + yIndex + "," + mapScale;
			tile._image = this.getBufferedImage(imageId);
			tile.id = imageId;
			if(!tile._image){
    			tile._imageSrc = url;
	            // 修正坐标不能放在此处修改
	            tile.initialize(xIndex, yIndex, mapScale, this._map, px, py, 5);
	            tile.id = imageId;
	            if(this._opacity < 100){
		            if(_GetBrowser() == 'ie'){
			            tile._tempImage.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._opacity) + ')';
		            }
	            }
    			
			    // 图片客户端缓存处理
			    if(this._enableImageBuffer){
				    if(this._imageBuffer.length > this._maxImageBufferCount){
					    var bufferCount = this._imageBuffer.length;
						var bufferIndex = 0;
						var count = this._maxImageBufferCount / 4;
						var j = 0;
						var nIndex = 0;
						while(j < bufferCount) {
							if(this._imageBuffer[nIndex].index < count) {
								var delObject = this._imageBuffer.splice(nIndex, 1);
								delObject.image.onload = null;
								delObject.image.onerror = null;
								delObject.image.onmousedown = null;
								if(delObject.image.parentNode) {
									delObject.image.parentNode.removeChild(delObject.image);
								}
								delete delObject;
								delObject = null;
							} else {
								this._imageBuffer[nIndex].index -= count;
								nIndex++;
							}
							j++;
						}
						this._index -= count;
				    }
    				
				    if(tile._tempImage && this.getBufferedImage(tile.id) == null){
				        if(!this._index){
				            this._index = 0;
				        }
						var imageObject = new Object();
						imageObject.image = tile._tempImage;
						imageObject.id = tile.id;
						imageObject.index = this._index++;
						this._imageBuffer.push(imageObject);
				    } else if(tile._image && this.getBufferedImage(tile.id) == null) {
				        if(!this._index){
				            this._index = 0;
				        }
						var imageObject = new Object();
						imageObject.image = tile._image;
						imageObject.id = tile.id;
						imageObject.index = this._index++;
				        this._imageBuffer.push(imageObject);
				    }
			    }
			}else{
       			tile._imageSrc = tile._image.src;
	            // 修正坐标不能放在此处修改
	            tile.initialize(xIndex, yIndex, mapScale, this._map, px, py, 5);
	            tile.id = imageId;
	            if(this._opacity < 100){
		            if(_GetBrowser() == 'ie'){
			            tile._tempImage.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._opacity) + ')';
		            }
	            }
			}
		}catch(e){
			//alert(e);
			//Debug("ex:" + e);
		}
		
		return tile;
	},
	render:function(width, height, originX, originY, offsetX, offsetY){
		/// <summary>将 GIS 数据图片显示在图片图层中。
		/// </summary>
		/// <param name="width" type="Number">呈现TiledLayer的容器的宽度。</param>
		/// <param name="height" type="Number">呈现TiledLayer的容器的高度。</param>
		/// <param name="originX" type="Number">map对象初始化时原点（左上角）的X坐标值。
		/// 该参数主要用于给该图层提供一个坐标参照系。</param>
		/// <param name="originY" type="Number" >map对象初始化时原点（左上角）的Y坐标值。
		/// 该参数主要用于给该图层提供一个坐标参照系。</param>
		/// <param name="offsetX" type="Number">待显示的图片原点（左上角）与初始化原点originX的偏移量。</param>
		/// <param name="offsetY" type="Number">待显示的图片原点（左上角）与初始化原点originY的偏移量。</param>
		// 需要计算偏差
		this._pixelOffsetX = offsetX;
		this._pixelOffsetY = offsetY;
		this._originX = originX;
		this._originY = originY;
		this._offsetX = offsetX;
		this._offsetY = offsetY;
		var mapScale = this._map.get_mapScale();
		var zoomLevel = this._resolveZoomLevel(mapScale);
		var resolution = this._resolutions[zoomLevel];
		var originCenter = new SuperMap.Point2D(0, 0); 
		var originPixel = this._map.mapCoordToPixel(originCenter, this._map.get_mapScale());
		var curMapCenter = this._map.get_mapParam().center;
		var curPixelCenter = this._map.get_mapParam().getPixelCenter(this._map.get_mapScale());
		var pixelOffset = new SuperMap.Point();
		// 地图的宽度
		var mapLength = this._tileLengths[zoomLevel];
		// 最左上角的像素单位
		pixelOffset.x = curPixelCenter.x - originPixel.x + mapLength/2;
		pixelOffset.y = curPixelCenter.y - originPixel.y + mapLength/2;
		// 对应级别有多少个格子
		var _tileCount = Math.pow(2, zoomLevel);
		// 左上角起始点
		var curStartPoint = new SuperMap.Point();
		curStartPoint.x = pixelOffset.x - width/2 - this._bufferSize * this._tileSize;
		curStartPoint.y = pixelOffset.y - height/2- this._bufferSize * this._tileSize;
	    // 右下角对应结束点
		var endPoint = new SuperMap.Point();
		endPoint.x = pixelOffset.x + width/2 + this._bufferSize * this._tileSize;
		endPoint.y = pixelOffset.y + height/2 + this._bufferSize * this._tileSize;
		// tile 起始和结束的索引
		var tileStartX = Math.floor(curStartPoint.x / this._tileSize);
		var tileStartY = Math.floor(curStartPoint.y / this._tileSize);
		var tileEndX = Math.ceil(endPoint.x / this._tileSize);
		var tileEndY = Math.ceil(endPoint.y / this._tileSize);
		// Debug(tileStartX + "," + tileStartY + " end:" + tileEndX + "," + tileEndY);
		var gridIndexOffset = new SuperMap.Point();
		gridIndexOffset.x = pixelOffset.x / mapLength;
		gridIndexOffset.y = pixelOffset.y / mapLength;
		pixelOffset.x = pixelOffset.x % mapLength;
		pixelOffset.y = pixelOffset.y % mapLength;
		var ratio = 1;
		// 设置tile计算的参数，如起始位置，像素点等
		var _offsetX = 0;
		var _offsetY = 0;
		var minRows = tileEndX - tileStartX;
		var minCols = tileEndY - tileStartY;
		var gridXIndex = tileStartX;
		var gridYIndex = tileStartY;
		// offset
		var tileoffsetx = originPixel.x - mapLength/2 + gridXIndex * this._tileSize - originX;
		var tileoffsety = originPixel.y - mapLength/2 + gridYIndex * this._tileSize - originY;
		var startPixel = new SuperMap.Point();
		startPixel.x = originPixel.x - mapLength/2 - originX;
		startPixel.y = originPixel.y - mapLength/2 - originY;
		//------------------------------
		var _tilesize = Math.floor(this._tileSize * ratio);
		
		// ---------------------
		// 移除不再需要的图片节点
		if(!this._preMapScale) {
			this._preMapScale = this._map.get_referMapScale();
			this._curMapScale = this._map.get_mapScale();
		} else {
			this._preMapScale = this._curMapScale;
			this._curMapScale = this._map.get_mapScale();
		}
		
		var curMapScale = "" + this._map.get_mapScale();
		var preMapScale = "" + this._preMapScale;
		
		if(curMapScale == preMapScale) {
			this._panCount++;
		} else {
			this._panCount = 0;
		}
		
		// 计算tiles		
		if((!this._tiles) || (this._tiles.length == 0)){
		    this._minXIndex = gridXIndex;
		    this._minYIndex = gridYIndex;
			this._tiles = null;
		    this._tiles = new Array();
		    this._tileStartX = gridXIndex;
			this._tileStartY = gridYIndex;
			this._tileEndX = gridXIndex + minRows;
		    this._tileEndY = gridYIndex + minCols;
		    this._tileCountX = this._tileEndX - this._tileStartX + 1;
			this._tileCountY = this._tileEndY - this._tileStartY + 1;
			
			if(this._enableSpiralLoad){
				var  tempTiles = new Array();
				// 建立临时tiles用于建立螺旋加载的索引序列
				for(var y = this._tileStartY; y <= this._tileEndY; y++){
					var tileRow = new Array();
					for(var x = this._tileStartX; x <= this._tileEndX; x++){
						var tile = new Object();
						tile.x = x;
						tile.y = y;
						tile.queued = false;
						tileRow.push(tile);
					}
					tempTiles.push(tileRow);
					var rows = new Array();
					this._tiles.push(rows);
				}
				// 螺旋加载相关参数
				var tileQueue = new Array();
				var directions = ["right", "down", "left", "up"];
				var iRow = 0;
				var iCell = -1;
				var direction = 0; // default right
				var directionsTried = 0;
				
				while( directionsTried < directions.length) {
						var testRow = iRow;
						var testCell = iCell;
						switch (directions[direction]) {
							case "right":
								testCell++;
								break;
							case "down":
								testRow++;
								break;
							case "left":
								testCell--;
								break;
							case "up":
								testRow--;
								break;
						}
						// 获取对应范围内的tile，并加入到queue队列中
						var tile = null;
						if ((testRow < this._tileCountY) && (testRow >= 0) &&
							(testCell < this._tileCountX) && (testCell >= 0)) {
							tile = tempTiles[testRow][testCell];
						}
						if ((tile != null) && (!tile.queued)) {
							// 加入到队列头部，最后进入队列的先加载
							tileQueue.unshift(tile);
							tile.queued = true;
							//restart the directions counter and take on the new coords
							directionsTried = 0;
							iRow = testRow;
							iCell = testCell;
						} else {
							// 加载其他方向的tile
							direction = (direction + 1) % 4;
							directionsTried++;
						}
					} 
					// 按照顺序进行加载tile
					for(var i=0; i < tileQueue.length; i++) {
						var tempTile = tileQueue[i];
						var _x = tileoffsetx + (tempTile.x-gridXIndex) * _tilesize;
			           var _y = tileoffsety + (tempTile.y-gridYIndex) * _tilesize;
			           var px = new SuperMap.Point(_x +_offsetX, _y +_offsetY);
			           tile = this._createTile(px, new SuperMap.Point(tempTile.x, tempTile.y), ratio);
						this._tiles[tempTile.y-this._tileStartY][tempTile.x-this._tileStartX] = tile;
					}
					tileQueue = null;
					// 清除临时tiles
					while(tempTiles.length > 0){
						var tileRow = tempTiles.pop();
						while(tileRow.length > 0){
							var tempTile = tileRow.pop();
							tempTile = null;
						}
						tileRow = null;
					}
					tempTiles = null;
			} else {
			    for(var i = 0; i < this._tileCountY; i++) {
			        var tileRow = new Array(); 
			        for(var j = 0; j < this._tileCountX; j++) {
			            var _x = tileoffsetx + j * _tilesize;
			            var _y = tileoffsety + i * _tilesize;
			            var px = new SuperMap.Point(_x +_offsetX, _y +_offsetY);
			            tile = this._createTile(px, new SuperMap.Point(gridXIndex + j, gridYIndex + i), ratio);
			            tileRow.push(tile);
			        }
			        this._tiles.push(tileRow);
			    }
			}
			
			// 解决centerAndZoom的时候造成的图片叠加问题，清除叠加的图片
			if((curMapScale == preMapScale)) {
				var j = 0;
				var buffer = Math.round(512 / this._tileSize);
				buffer = 0;
				while(j < this._container.childNodes.length) {
					var tileId = this._container.childNodes[j].id;
					var tileIdParts = tileId.split(",");
					var inrange = true;
					if(tileIdParts != null && tileIdParts.length > 0) {
						var xIndex = parseInt(tileIdParts[0]);
						var yIndex = parseInt(tileIdParts[1]);
						if(xIndex < this._tileStartX - buffer || xIndex > this._tileEndX + buffer || yIndex < this._tileStartY - buffer || yIndex > this._tileEndY + buffer) {
							inrange = false;
						}
					}
					if(!inrange) {
						this._container.childNodes[j].onload = null;
						this._container.childNodes[j].onerror = null;
						this._container.childNodes[j].onmousedown = null;
						this._container.removeChild(this._container.childNodes[j]);
					} else {
						j++;
					}
				}
			}
		} else {
			// 清除 oldTiles
			this._clearTiles(this._oldTiles, true);
			this._oldTiles = null;
		    // 本次tile索引的起始位置
		    this._minXIndex = gridXIndex;
			this._minYIndex = gridYIndex;
			var maxXIndex = this._minXIndex + minRows;
			// maxYIndex
			var maxYIndex = this._minYIndex + minCols;
			
			// 不同地图的时候需要重新计算
			this._tileCountX = maxXIndex - this._minXIndex + 1;
			this._tileCountY = maxYIndex - this._minYIndex + 1;
			// 重新计算后的x方向起始索引大于上一次x方向起始索引，那么需要将左边多余的tile移除
			while(this._tileStartX < this._minXIndex){
				// 移除对应列中的起始记录
				for(var y = this._tiles.length - 1; y >= 0; y--){
					var tile = this._tiles[y].shift();
					if(tile){
						this._removeFromMap(tile);
					}
				}
				this._tileCountX--;
				this._tileStartX++;
				tileoffsetx += Math.floor(this._tileSize*ratio);
			}
			// 重新计算后的x方向起始索引小于上一次x方向起始索引，那么需要补上左边欠缺的tile
			while(this._tileStartX > this._minXIndex){
				// 对应列中的起始位置插入新的记录
				this._tileStartX--;
				this._tileCountX++;
				tileoffsetx -= Math.floor(this._tileSize*ratio);
				for(var y = 0; y < this._tiles.length; y++){
					var _x = startPixel.x + this._tileStartX * _tilesize;
				    _x -= 0; 
				    // caculate layer div offset x
				    var _y = startPixel.y + (this._tileStartY+y) * _tilesize;
				    _y -= 0; 
				    // caculate layer div offset y
				    var px = new SuperMap.Point(_x, _y);				    
					var tile=this._createTile(px, new SuperMap.Point(this._tileStartX, this._tileStartY + y), ratio);
					
					this._tiles[y].unshift(tile);
				}
			}
			// 重新计算后的x方向终止索引小于上一次x方向终止索引，那么需要将右边多余的tile移除
			while(this._tileEndX > maxXIndex){
				// 移除对应列中的尾部记录
				for(var y = 0; y < this._tiles.length; y++){
					var tile = this._tiles[y].pop();
					if(tile){
						this._removeFromMap(tile);
					}
				}
				this._tileEndX--;
				this._tileCountX--;
			}
			// 重新计算后的x方向终止索引大于上一次x方向终止索引，那么需要将右边欠缺的tile补上
			while(this._tileEndX < maxXIndex){
				// 对应列中的尾部插入新的记录
				this._tileEndX++;
				this._tileCountX++;
				for(var y = 0; y < this._tiles.length; y++){
					var _x = startPixel.x + this._tileEndX * _tilesize;
				    _x -= 0; 
				    // caculate layer div offset x
				    var _y = startPixel.y + (this._tileStartY+y) * _tilesize;
				    _y -= 0; 
				    // caculate layer div offset y
    	
				    var px = new SuperMap.Point(_x, _y);
				    // caculate layer div offset
					var tile=this._createTile(px, new SuperMap.Point(this._tileEndX, this._tileStartY+y), ratio);
					this._tiles[y].push(tile);
				}
			}
			// 重新计算后的y方向起始索引大于上一次y方向起始索引，那么需要将上边多余的tile移除
			while(this._tileStartY < this._minYIndex){
				// 在整个队列的起始处，移除对应行的记录
				var tileRow = this._tiles.shift();
				if(tileRow){
					while(tileRow.length > 0){
						var tile = tileRow.pop();
						this._removeFromMap(tile);
					}
					tileRow = null;
				}
				this._tileStartY++;
				tileoffsetx -= Math.floor(this._tileSize*ratio);
				this._tileCountY--;
			}
			// 重新计算后的y方向起始索引小于上一次y方向起始索引，那么需要将上边欠缺的tile补上
			while(this._tileStartY > this._minYIndex){
				// 在整个队列的起始处插入对应行的记录
				this._tileStartY--;
				this._tileCountY++;
				tileoffsety += Math.floor(this._tileSize * ratio);
				var tileRow = new Array();
				for(var i = 0; i < this._tileCountX; i++){
					var _x = startPixel.x + (this._tileStartX + i) * _tilesize;
				    // caculate layer div offset x
				    var _y = startPixel.y + this._tileStartY * _tilesize;
				    // caculate layer div offset y
    	
				    var px = new SuperMap.Point(_x, _y);
					var tile=this._createTile(px, new SuperMap.Point(this._tileStartX + i, this._tileStartY), ratio);
					tileRow.push(tile);
				}
				this._tiles.unshift(tileRow);
			}
			// 重新计算后的y方向终止索引小于上一次y方向终止索引，那么需要将下边多余的tile移除
			while(this._tileEndY > maxYIndex){
				// 在整个队列的尾部，移除对应行的记录
				var tileRow = this._tiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile){
						this._removeFromMap(tile);
					}
				}
				this._tileEndY--;
				this._tileCountY--;
			}
			// 重新计算后的y方向终止索引大于上一次y方向终止索引，那么需要将下边欠缺的tile补上
			while(this._tileEndY < maxYIndex){
				// 在整个队列的尾部，添加对应行的记录
				this._tileEndY++;
				this._tileCountY++;
				var tileRow = new Array();
				for(var x = 0; x < this._tileCountX; x++){
				    var _x = startPixel.x + (this._tileStartX + x) * _tilesize;
				    // caculate layer div offset x
				    var _y = startPixel.y + this._tileEndY * _tilesize;
				    // caculate layer div offset y
    	
				    var px = new SuperMap.Point(_x, _y);
					var tile=this._createTile(px, new SuperMap.Point(this._tileStartX + x, this._tileEndY), ratio);
					tileRow.push(tile);
				}
				this._tiles.push(tileRow);
			}
			// 校验tiles，删除多余的tile，同时补上缺失的tile
			for(var i=0; i<this._tileCountY; i++){
				var tileRow = this._tiles[i];
				if(tileRow){
				    var index = 0;
					while(tileRow.length < this._tileCountX){
					   var preTile = tileRow[tileRow.length-1];
					   if(preTile) {
					        var _x = preTile._curPositionX + Math.floor(this._tileSize * ratio);
					        var _y = preTile._curPositionY;
					        var px = new SuperMap.Point(_x +_offsetX, _y +_offsetY);
					        var tile=this._createTile(px, new SuperMap.Point(preTile._indexX+1, preTile._indexY), ratio);
					   } else {
					       var _x = startPixel.x + (this._tileStartX + index) * _tilesize;
				           // caculate layer div offset x
				           var _y = startPixel.y + (gridYIndex + i) * _tilesize;
				           // caculate layer div offset y
				            var px = new SuperMap.Point(_x, _y);
					        var tile=this._createTile(px, new SuperMap.Point(this._tileStartX+index, gridYIndex + i), ratio);
					    }
					    index++;
						tileRow.push(tile);
					}
					
					while(tileRow.length > this._tileCountX){
						var tile=tileRow.pop();
						if(tile){
							this._removeFromMap(tile);
							tile = null;
						}
					}
				} else{
					tileRow = new Array();
					for(var j = 0; j < this._tileCountX; j++){
						var _x = startPixel.x + (this._tileStartX + j) * _tilesize;
			           // caculate layer div offset x
			           var _y = startPixel.y + (gridYIndex + i) * _tilesize;
			           // caculate layer div offset y
			           var px = new SuperMap.Point(_x, _y);
					    var tile=this._createTile(px, new SuperMap.Point(this._tileStartX + j, gridYIndex  + i), ratio);
						tileRow.push(tile);
					}
					this._tiles.push(tileRow);
				}				
			}
		}
		
		if(this._tiles) {
		    for(var i = 0; i < this._tiles.length; i++) {
		        if(this._tiles[i] && this._tiles[i].length == 0) {
				    this._tiles.pop();
			    }
			}
		}
		
		if(curMapScale !== preMapScale) {
			var j = 0;
			var length = this._container.childNodes.length;
			while(j < this._container.childNodes.length) {
				var tileId = this._container.childNodes[j].id;
				if(tileId && tileId.indexOf(curMapScale) == -1) {
					this._container.childNodes[j].onload = null;
					this._container.childNodes[j].onerror = null;
					this._container.childNodes[j].onmousedown = null;
					this._container.childNodes[j].onmouseover = null;
					this._container.childNodes[j].onclick = null;
					this._container.removeChild(this._container.childNodes[j]);
					
					// alert(j);
				} else {
					j++;
				}
			}
		}
	},
	getType:function(){
		return "SuperMap.GlobalMapLayer";
	}
};
SuperMap.GlobalMapLayer.registerClass('SuperMap.GlobalMapLayer', SuperMap.TiledLayerBase, Sys.IDisposable);

SuperMap.MercatorProjection = function(zoomLevels){
	/// <summary>摩卡托投影对象。</summary>
	/// <param name="zoomLevels" type="Number">放大级别的总数目。</param>
	this._resolutions = [];
	this._radius = [];
	this._referPoints = [];
	this._sizes = [];
	var size = 256;
	for(var d = 0; d < zoomLevels; d++){
		var e = size/2;
		this._resolutions.push(size/360);
		this._radius.push(size/(2*Math.PI));
		this._referPoints.push(new SuperMap.Point(e,e));
		this._sizes.push(size);
		size*=2;
	}
};
SuperMap.MercatorProjection.prototype = {
	latLngToPixel:function(latLng, zoomLevel){
		/// <summary>将经纬度转换成地图像素坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		var referPoint = this._referPoints[zoomLevel];
		var x = Math.round(referPoint.x + latLng.x*this._resolutions[zoomLevel]);
		var rate = Math.sin(latLng.y*(Math.PI/180));
		rate = Math.max(f, -0.9999);
		rate = Math.min(f, 0.9999);
		var y = Math.round(referPoint.y + 0.5*Math.log((1+rate)/(1-rate))*- this._radius[zoomLevel]);
		
		return new SuperMap.Point(x, y);
	},
	pixelToLatLng:function(point,zoomLevel){
		/// <summary>将地图像素坐标转换成经纬度坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		var referPoint = this._referPoints[zoomLevel];
		var x = (point.x - referPoint.x)/this._resolutions[zoomLevel];
		var rate = (point.y - referPoint.y)/-this._radius[zoomLevel];
		var y = (4*Math.atan(Math.exp(rate))-Math.PI) * 90 / Math.PI;

		return new SuperMap.Point2D(x, y);
	},
	dispose:function(){
		/// <summary>释放所占用的资源。
		/// </summary>
		if(this._resolotions) {
			while(this._resolutions.length > 0) {
				this._resolutions.pop();
				this._radius.pop();
				this._referPoints.pop();
				this._sizes.pop();
			}
		}
	}
};
SuperMap.MercatorProjection.registerClass("SuperMap.MercatorProjection", null, Sys.IDisposable);