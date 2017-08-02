/**
 * @description 店铺内分类管理
 */
define([
    'cms'
], function (cms) {

    function getNodeByName(catName, tree) {

        if (tree == null)
            return null;

        let result = null;

        _.find(tree, function (element) {
            if (element.catName && element.catName == catName) {
                result = element;
                return true;
            }

            if (element.children && element.children.length) {
                result = getNodeByName(catName, element.children);
                if (result)
                    return true;
            }

        });

        return result;
    }

    cms.controller('shopCategoryController', class ShopCategoryController {

        constructor(sellerCatService, popups) {
            this.sellerCatService = sellerCatService;
            this.popups = popups;
            this.totalCategory = [];
        }

        init() {
            let self = this;

            self.sellerCatService.getCat({cartId: 8}).then(res => {
                let len = self.totalCategory.length;
                self.totalCategory.push({index: len, children: res.data.catTree});

            });
        }

        /**
         * @param category 子节点
         * @param categoryItem 父节点
         */
        openCategory(category, categoryItem) {
            const self = this,
                totalCategory = self.totalCategory;

            totalCategory.splice(categoryItem.index + 1);

            if (category && category.children.length > 0) {
                let len = totalCategory.length;
                totalCategory.push({index: len, children: category.children});
            }

            categoryItem.selectedCat = category;
            self.selected = category;

        }

        popEditCategory(model, $event) {
            let self = this;

            self.popups.openEditCategory(model).then(res => {

            });

            $event.stopPropagation();
        }

        popIncreaseCategory(categoryItem) {
            let self = this,
                index = categoryItem.index,
                selectedCat,
                totalCategory = self.totalCategory;

            if(index !== 0){
                selectedCat = totalCategory[index-1].selectedCat;
            }

            self.popups.openIncreaseCategory({
                selectObject: selectedCat
            }).then(response => {

                let parentCatId = index === 0 ? 0 : selectedCat.catId;

                self.sellerCatService.addCat({
                    "cartId": 8,
                    "catName": response.catName,
                    "parentCatId": parentCatId
                }).then(function (res) {
                    let newNode = getNodeByName(response.catName, res.data.catTree);

                    if (index === 0)
                        self.totalCategory[0].children.push(newNode);
                    else
                        selectedCat.children.push(newNode);

                });
            })
        }

        /**
         *
         * @param parentNode    父节点
         * @param parentCatId   父节点catId
         * @param catName
         */
        save(root, parentNode, parentCatId, catName) {
            var self = this;

            this.selected[this.newIndex.value] = catName;

        };

    })

});