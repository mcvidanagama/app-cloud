/*
 *
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

// page initialization
$(document).ready(function () {
    getExposureLevel();
    getReplicaCountForVersion();
});

$("#update-default-version").click(function () {
    var versionName = $("#default-version option:selected").val();

    jagg.post("../blocks/settings/settings.jag", {
        action: "updateDefaultVersion",
        applicationName: applicationName,
        defaultVersion: versionName
    },function defaultVersionUpdatedSuccess(result) {
        //alert(result);
        var defaultVersion = "Default version is set to " + $("#default-version option:selected").val();
        $("#lbl-default-version").text(defaultVersion);
        jagg.message({content: "Default version is successfully updated.", type: 'success', id: 'view_log'});
        setTimeout(redirectAppHome, 3000);
        function redirectAppHome(){
            window.location.replace("home.jag?applicationKey=" + result);
        }
    },function (jqXHR, textStatus, errorThrown) {
        jagg.message({content: jqXHR.responseText, type: 'error', id: 'view_log'});

    });
});


$("#update-exposure-level").click(function () {
    updateVersionExposureLevelPopUp();
});

$("#exposure-level-version-list").change(function () {
    getExposureLevel();
});

function getExposureLevel() {
    var versionName = $("#exposure-level-version-list option:selected").val();
    jagg.post("../blocks/settings/settings.jag", {
        action:"getExposureLevel",
        versionName:versionName,
        applicationName:applicationName
    },function (result) {
        $("#security-" + result).prop("checked", true);
    },function (jqXHR, textStatus, errorThrown) {
        jagg.message({content: "Error occurred while retrieving the endpoint security level of the selected "
                    + cloudSpecificApplicationRepresentation.toLowerCase() + " version", type: 'error', id:'view_log'});
    });
}

$("#scale-deployment").click(function () {
    scaleDeploymentPopUp();
});

$(document).on('click', '.number-spinner a', function () {
	var btn = $(this),
		oldValue = btn.closest('.number-spinner').find('input').val().trim(),
		input = btn.closest('.number-spinner').find('input'),
		newVal = 1;

	if (btn.attr('data-dir') == 'up') {
	    console.log(oldValue);

    if (oldValue == 8 ) {
        btn.prop("disabled", true);
    } else {
        btn.prop("disabled", false);
        if (parseInt(oldValue) < parseInt(input.attr('max'))){
            newVal = parseInt(oldValue) + 1;
        }
    }

	} else {
		if (oldValue > 1) {
			newVal = parseInt(oldValue) - 1;
		} else {
			newVal = 1;

		}
	}
	btn.closest('.number-spinner').find('input').val(newVal);
	setRemainingInstanceCount(maxReplicaCount, newVal);
});

$("#scale-deployment-list").change(function () {
    getReplicaCountForVersion();
});

function getReplicaCountForVersion() {
    var versionName = $("#scale-deployment-list option:selected").val();
    jagg.post("../blocks/settings/settings.jag", {
        action: "getReplicaCountForVersion",
        applicationName: applicationName,
        versionName: versionName
    },function scaleDeploymentSuccess(result) {
        $("#replica-count").val(result);
        setRemainingInstanceCount(maxReplicaCount, result);

    },function (jqXHR, textStatus, errorThrown) {
        jagg.message({content: jqXHR.responseText, type: 'error', id: 'view_log'});
    });
}

function setRemainingInstanceCount(replicaLimit, replicaCount){
    var remainingInstances = replicaLimit - replicaCount;
    $('#remaining-instances').html("<span>" + remainingInstances + " remaining instances <a href=\"" + requestIncreaseReplicaLimitURL + "\">(Increase replica limit)</a></span>");
}

function scaleDeploymentPopUp(){
    jagg.popMessage({type:'confirm', modalStatus: true, title:'Scale Deployment ' + cloudSpecificApplicationRepresentation + ' Version',content:'Are your sure you want to scale ' +
        $("#scale-deployment-list option:selected").val() + ' version of this ' + cloudSpecificApplicationRepresentation.toLowerCase() +
        ' to ' + $("#replica-count").val() + ' ?',
        yesCallback:function(){
           scaleDeployment();
        }
    });
}

function scaleDeployment(){
    var versionName = $("#scale-deployment-list option:selected").val();
    jagg.post("../blocks/settings/settings.jag", {
        action: "scaleDeployment",
        applicationName: applicationName,
        versionName: versionName,
        replicaCount: $("#replica-count").val()
    },function (result) {
        jagg.message({content: "Auto scale policy is successfully updated.", type: 'success', id: 'view_log'});

    },function (jqXHR, textStatus, errorThrown ) {
        jagg.message({content: jqXHR.responseText, type: 'error', id: 'view_log'});
    });
}

function updateVersionExposureLevelPopUp(){
    jagg.popMessage({type:'confirm', modalStatus: true, title:'Privacy level of ' + cloudSpecificApplicationRepresentation + ' Version',content:'Are your sure you want to change privacy level of ' +
        $("#exposure-level-version-list option:selected").val() + ' version of this ' + cloudSpecificApplicationRepresentation.toLowerCase() +
        ' to ' + $("input:radio[name='security-radio']:checked").val() + ' ?',
        yesCallback:function(){
           updateVersionExposureLevel();
        }
    });
}

function updateVersionExposureLevel(){
    var versionName = $("#exposure-level-version-list option:selected").val();
    jagg.post("../blocks/settings/settings.jag", {
        action: "updateVersionExposureLevel",
        applicationName: applicationName,
        versionName: versionName,
        exposureLevel: $("input:radio[name='security-radio']:checked").val()
    },function exposureLevelUpdatedSuccess(result) {
        jagg.message({content: "Application privacy settings successfully updated", type: 'success', id: 'view_log'});

    },function (jqXHR, textStatus, errorThrown) {
        jagg.message({content: jqXHR.responseText, type: 'error', id: 'view_log'});
    });
}

$(function(){
    $('.spinner .btn:first-of-type').on('click', function() {
      var btn = $(this);
      var input = btn.closest('.spinner').find('input');
      if (input.attr('max') == undefined || parseInt(input.val()) < parseInt(input.attr('max'))) {
        input.val(parseInt(input.val(), 10) + 1);
        setRemainingInstanceCount(maxReplicaCount, input.val());
      } else {
        btn.next("disabled", true);
      }
    });
    $('.spinner .btn:last-of-type').on('click', function() {
      var btn = $(this);
      var input = btn.closest('.spinner').find('input');
      if (input.attr('min') == undefined || parseInt(input.val()) > parseInt(input.attr('min'))) {
        input.val(parseInt(input.val(), 10) - 1);
        setRemainingInstanceCount(maxReplicaCount, input.val());
      } else {
        btn.prev("disabled", true);
      }
    });

});