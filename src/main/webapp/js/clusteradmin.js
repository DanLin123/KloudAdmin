function executeMoveToken(selection){
	$.ajax({
		  url: $('#move-dialog').attr('executeMoveTokenURL'),
		  type: 'POST',
		  dataType: 'json',
		  data: {'selection': selection},
		  success: function(data) {
			  $("#node-info-pangel-range").text(data.value);
			  $.jGrowl(data.message);
		  },
		  error: function(){
			  $.jGrowl('Something went wrong while moving the token.');
		  }
	});
}



function executeConfirm(urlinput,buttonName,text){
	$( "#dialog-confirm #dialog-comfirm-text" ).text(text);
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:140,
		modal: true,
		buttons: {
			Yes: function() {
				$.ajax({
					  url:urlinput,
					  type: 'POST',
					  dataType: 'json',
					  success: function(data) {
						  $.jGrowl(data.message);
					  },
					  error: function(){
						  $.jGrowl('Something went wrong while '+buttonName);
					  }
				});
				$( this ).dialog( "close" );
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		}
	});
}




