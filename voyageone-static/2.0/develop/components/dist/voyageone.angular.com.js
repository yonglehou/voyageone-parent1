define([
  'components/angular/controllers/selectRows.ctrl',
  'components/angular/controllers/showPopover.ctrl',
  'components/angular/directives/dateModelFormat.directive',
  'components/angular/directives/enterClick.directive',
  'components/angular/directives/filestyle.directive',
  'components/angular/directives/ifNoRows.directive',
  'components/angular/directives/keyValue.directive',
  'components/angular/directives/navigation.directive',
  'components/angular/directives/popoverText.directive',
  'components/angular/directives/schema.directive',
  'components/angular/directives/tabInTextarea.directive',
  'components/angular/directives/voption.directive',
  'components/angular/directives/vpagination.directive',
  'components/angular/directives/vvalidator.directive',
  'components/angular/factories/dialogs.factory',
  'components/angular/factories/interceptor.factory',
  'components/angular/factories/notify.factory',
  'components/angular/factories/pppAutoImpl.factory',
  'components/angular/factories/selectRows.factory',
  'components/angular/factories/templates.factory',
  'components/angular/factories/vpagination.factory',
  'components/angular/providers/vresources',
  'components/angular/services/ajax.service',
  'components/angular/services/cookie.service',
  'components/angular/services/message.service',
  'components/angular/services/permission.service',
  'components/angular/services/translate.service'
], function () {

 angular.module("voyageone.angular",[
"voyageone.angular.controllers.selectRows",
"voyageone.angular.controllers.showPopover",
"voyageone.angular.directives.dateModelFormat",
"voyageone.angular.directives.enterClick",
"voyageone.angular.directives.fileStyle",
"voyageone.angular.directives.ifNoRows",
"voyageone.angular.directives.keyValue",
"voyageone.angular.directives.uiNav",
"voyageone.angular.directives.popoverText",
"voyageone.angular.directives.schema",
"voyageone.angular.directives.tabInTextarea",
"voyageone.angular.directives.voption",
"voyageone.angular.directives.vpagination",
"voyageone.angular.directives.validator",
"voyageone.angular.factories.dialogs",
"voyageone.angular.factories.interceptor",
"voyageone.angular.factories.notify",
"voyageone.angular.factories.pppAutoImpl",
"voyageone.angular.factories.selectRows",
"voyageone.angular.factories.templates",
"voyageone.angular.factories.vpagination",
"voyageone.angular.services.ajax",
"voyageone.angular.services.cookie",
"voyageone.angular.services.message",
"voyageone.angular.services.permission",
"voyageone.angular.services.translate"
]);

});