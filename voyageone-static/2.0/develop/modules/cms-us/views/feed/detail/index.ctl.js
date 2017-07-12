/**
 * @description feed detail
 */
define([
    'cms'
], function (cms) {

    cms.controller('feedDetailController', class FeedDetailController {
        constructor(popups, $routeParams, itemDetailService, alert,$location,notify,confirm,$rootScope,$sessionStorage) {
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;
            this.notify = notify;
            this.confirm = confirm;
            this.$sessionStorage = $sessionStorage;
            this.auth = this.$sessionStorage.auth.auth;

            this.id = $routeParams.id;
            if (!this.id) {
                this.alert("Feed not exists.");
                return;
            }

            this.feed = {};
            this.brandList = [];
            this.productTypeList = [];
            this.sizeTypeList = [];
            this.materialList = [{value: "wood", name: "wood"}];
            this.originList = [{value: "CN", name: "CN"}];
            this.colorMap = [{value: "Red", name: "Red"}];
            this.setting = {
                weightOrg: "",
                weightOrgUnit: "lb",
                priceClientMsrp: "",
                priceNet: "",
                priceMsrp: "",
                priceCurrent: "",

                weightOrgUnits: ['kg', 'lb']
            };
            this.topFeedList = null; // 同Model查询结果
            this.imageUrl = "http://image.sneakerhead.com/is/image/sneakerhead/";
            this.init();
        }

        init() {
            let self = this;
            // 根据code加载Feed
            self.itemDetailService.detail({id: self.id}).then((resp) => {
                if (resp.data && resp.data.feed) {
                    self.feed = resp.data.feed;
                    // 处理Feed数据
                    self.filterFeed();
                    self.brandList = resp.data.brandList;
                    self.productTypeList = resp.data.productTypeList;
                    self.sizeTypeList = resp.data.sizeTypeList;

                    // TODO 配置项，暂时无数据
                    // self.materialList = resp.data.materialList;
                    // self.originList = resp.data.originList;
                    // self.colorMap = resp.data.colorMap;
                    if (self.feed && _.size(self.feed.image) > 0) {
                        self.currentFeedImage = self.feed.image[0];
                        _.extend(self.feed, {imageNum:_.size(self.feed.image)});
                    }

                    if (self.feed && self.feed.attribute.boximages && _.size(self.feed.attribute.boximages) > 0) {
                        self.currentBoxImage = self.feed.attribute.boximages[0];
                        _.extend(self.feed, {boxImageNum:_.size(self.feed.attribute.boximages)});
                    }

                } else {
                    let id = self.id;
                    let message = `Feed(id:${id}) not exists.`;
                    self.alert(message);
                    // self.alert(message).then((res) => {
                    //     self.$location.path("");
                    // });
                }
            });
        }

        // 处理Feed数据
        filterFeed() {
            let self = this;
            let hasUrlkey = !!self.feed.attribute.urlkey && _.size(self.feed.attribute.urlkey) > 0;
            _.extend(self.feed, {hasUrlkey: hasUrlkey});
            if (!hasUrlkey) {
                // 计算urlKey
                self.generateUrlKey();
            }
            // 将sku->weightOrg转换成float
            angular.forEach(self.feed.skus, function (sku) {
                sku.weightOrg = parseFloat(sku.weightOrg);
            });
            // 处理Abstract和accessory
            if (!!self.feed.attribute.abstract && _.size(self.feed.attribute.abstract) > 0) {
                _.extend(self.feed, {abstract: self.feed.attribute.abstract[0]});
            }
            if (!!self.feed.attribute.accessory && _.size(self.feed.attribute.accessory) > 0) {
                _.extend(self.feed, {accessory: self.feed.attribute.accessory[0]});
            }
            // 处理orderlimitcount
            if (!!self.feed.attribute.orderlimitcount && _.size(self.feed.attribute.orderlimitcount) > 0) {
                _.extend(self.feed, {orderlimitcount: self.feed.attribute.orderlimitcount[0]});
            }
            // 处理colorMap
            if (!!self.feed.attribute.colorMap && _.size(self.feed.attribute.colorMap) > 0) {
                _.extend(self.feed, {colorMap: self.feed.attribute.colorMap[0]});
            }
            // 处理amazonBrowseTree
            if (!!self.feed.attribute.amazonBrowseTree && _.size(self.feed.attribute.amazonBrowseTree) > 0) {
                _.extend(self.feed, {amazonBrowseTree: self.feed.attribute.amazonBrowseTree[0]});
            }
        }

        // 生成UrlKey
        generateUrlKey() {
            let self = this;
            if (!self.feed.hasUrlkey && self.feed.name) {
                let urlkey = self.feed.name + "-" + self.feed.code;
                urlkey = urlkey.replace(/[^a-zA-Z0-9]+/g, " ");
                urlkey = urlkey.replace(/\s+/g, "-");
                self.feed.attribute.urlkey = [urlkey.toLowerCase()];
            }
        }

        // 统一设置SKU属性
        setSkuProperty(sku, property) {
            let self = this;
            if (!sku) {
                angular.forEach(self.feed.skus, function (item) {
                    item[property] = self.setting[property];
                    item['priceClientRetail'] = item['priceNet'];
                })
            } else {
                sku['priceClientRetail'] = sku['priceNet'];
            }
            self.setSkuCNPrice();
        }

        // 同步计算中国相关价格
        setSkuCNPrice() {
            let self = this;
            let productType = self.feed.productType;
            self.itemDetailService.setPrice({feed: self.feed}).then((res) => {
                if (res.data) {
                    self.feed.skus = res.data.skus;
                    // 将sku->weightOrg转换成float
                    angular.forEach(self.feed.skus, function (sku) {
                        sku.weightOrg = parseFloat(sku.weightOrg);
                    });

                    var priceScope = {
                        priceClientRetailMin: res.data.priceClientRetailMin,
                        priceClientMsrpMin: res.data.priceClientMsrpMin,
                        priceClientRetailMax: res.data.priceClientRetailMax,
                        priceClientMsrpMax: res.data.priceClientMsrpMax
                    };
                    _.extend(self.feed, priceScope);
                }
            });
        }

        save(flag) {
            let self = this;

            // 如果是Approve,approve price是否打钩。价格是否为0或者500，如果是0或者500警告用户再次确认
            if (self.feed.status == 'Ready' && flag == '1') {
                if (self.feed.approvePricing != '1') {
                    self.alert("Please check 'Approve Pricing'.");
                    return;
                }
                // Msrp or price O或500时Confirm
                let checkSkus = _.filter(self.feed.skus, function (sku) {
                    return sku.priceClientMsrp == 0 || sku.priceClientMsrp == 500 || sku.priceNet == 0 || sku.priceNet == 500;
                });
                if (!checkSkus || _.size(checkSkus) == 0) {
                    let ctx = {
                        updateModel: true,
                        codeList: [self.feed.code]
                    };
                    self.popups.openBatchApprove(ctx).then((res) => {
                        if (res.success) {
                            _.extend(self.feed, {approveInfo: res.approveInfo});
                            self.saveFeed(flag);
                        }
                    });
                } else {
                    let skus = [];
                    angular.forEach(checkSkus, function (sku) {
                        skus.push(sku.sku);
                    });
                    let message = `SKU[${skus}] Msrp($) or price($) is 0 or 500, continue to Approve?`;
                    self.confirm(message).then((confirmed) => {
                        let ctx = {
                            updateModel: true,
                            codeList: [self.feed.code]
                        };
                        self.popups.openBatchApprove(ctx).then((res) => {
                            if (res.success) {
                                _.extend(self.feed, {approveInfo: res.approveInfo});
                                self.saveFeed(flag);
                            }
                        });
                    }, () => {

                    })
                }
            } else {
                self.saveFeed(flag);
            }
        }

        saveFeed(flag) {
            let self = this;
            // 处理Abstract和accessory
            self.feed.attribute.abstract = [self.feed.abstract];
            self.feed.attribute.accessory = [self.feed.accessory];
            // 处理orderlimitcount
            self.feed.attribute.orderlimitcount = [self.feed.orderlimitcount];
            // 处理colorMap
            self.feed.attribute.colorMap = [self.feed.colorMap];
            // 处理amazonBrowseTree
            self.feed.attribute.amazonBrowseTree = [self.feed.amazonBrowseTree];
            let parameter = {feed: self.feed, flag: flag};
            self.itemDetailService.update(parameter).then((res) => {
                if (res.data) {
                    self.notify.success("Operation succeeded.");
                    _.extend(self.feed, res.data);
                    // 处理Feed数据
                    self.filterFeed();
                }
            });
        }

        popUsCategory() {
            let self = this;

            self.popups.openUsCategory({
                from:self.feed.category
            }).then(context => {
                _.extend(self.feed, {category: context.catPath})
            });
        }

        initImage(num) {
            let self = this;
            let urlKey = "";
            if (self.feed.hasUrlkey && num > 0) {
                urlKey = self.feed.attribute.urlkey[0];
                if (!self.feed.image) {
                    self.feed.image = [];
                }
                let count = _.size(self.feed.image);
                let add = num - count;
                if (add > 0) {
                    for (let i = 1; i <= add; i++) {
                        self.feed.image.push(self.imageUrl + urlKey + "-" + (count + i));
                    }
                } else {
                    self.feed.image.splice(add);
                }
            }
        }

        addImage() {
            let self = this;
            if (!self.feed.image) {
                self.feed.image = [];
            }
            self.feed.image.push("");
        }

        deleteImage(index) {
            let self = this;
            self.feed.image.splice(index, 1);
        }

        initBoxImage(num) {
            let self = this;
            let urlKey = "";
            if (self.feed.hasUrlkey && num > 0) {
                urlKey = self.feed.attribute.urlkey[0];
                if (!self.feed.attribute.boximages) {
                    self.feed.attribute.boximages = [];
                }
                let count = _.size(self.feed.attribute.boximages);
                let add = num - count;
                if (add > 0) {
                    for (let i = 1; i <= add; i++) {
                        self.feed.attribute.boximages.push(self.imageUrl + urlKey + "-2" + (count + i));
                    }
                } else {
                    self.feed.attribute.boximages.splice(add);
                }
            }
        }

        addBoxImage() {
            let self = this;
            if (!self.feed.attribute.boximages) {
                self.feed.attribute.boximages = [];
            }
            self.feed.attribute.boximages.push("");
        }

        deleteBoxImage(index) {
            let self = this;
            self.feed.attribute.boximages.splice(index, 1);
        }

        // 同Model
        getTopModel(top) {
            let self = this;

            self.isShowModal = !self.isShowModal;

            if (self.feed.model) {
                self.itemDetailService.getTopModel({
                    code: self.feed.code,
                    model: self.feed.model,
                    top: top
                }).then((res) => {
                    if (res.data) {
                        self.topFeedList = res.data;
                        // 如果从Product查询的Code属性,那图片只是图片名称,不是完整链接
                        angular.forEach(self.topFeedList, feed => {
                            // _id为空则说明信息从Product查询而来
                            if (!feed._id) {
                               let newImage = [];
                                _.each(feed.image, image => {
                                    image = self.imageUrl + image;
                                   newImage.push(image);
                                });
                                feed.image = newImage;
                            }
                        });
                    }
                })
            }
        }

        // Copy其他code部分属性
        copyAttr(feed) {
            let self = this;

            let attribute = {
                brand: feed.brand,
                productType: feed.productType,
                sizeType: feed.sizeType,
                material: feed.material,
                origin: feed.origin,
                usageEn: feed.usageEn,
                shortDescription:feed.shortDescription,
                longDescription:feed.longDescription

            };
            _.extend(self.feed, attribute);

            self.notify.success('copy success!');
        }

        /**
         * @description 弹出亚马逊类目  cartId：5
         */
        popAmazonCategory(){
            let self = this;

            self.popups.openAmazonCategory().then(res => {
                self.feed.attribute.amazonBrowseTree = res.catPath;
            });
        }

        goDetail(url) {
            if (!url)
                return;

            window.open(url);
        }

    });

});