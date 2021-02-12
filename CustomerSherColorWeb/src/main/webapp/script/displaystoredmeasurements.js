
function displayStoredMeasurementsTable(userLocale){
	var langUrl = null;
	// translate datatable text and make it sortable by that locale's date format 
	switch(userLocale){
		case("es_ES"):
			langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json";
			$.fn.dataTable.moment('DD-MMM-YYYY H:mm:ss', 'es');
			break;
		case("zh_CN"):
			langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Chinese.json";
			$.fn.dataTable.moment('YYYY-M-D H:mm:ss', 'zh-cn');
			break;
		default:
			$.fn.dataTable.moment('MMM D, YYYY h:mm:ss A', 'en');
	}
	
	measurementTable = $("#measurement_table").DataTable({
		dom: 'ifBrtp',
		"language": {
        	"emptyTable" : i18n['displayStoredMeasurements.noMeasurementsAvailable'],
        	"url" : langUrl
        },
        "ordering": true,
        "order": [ 2, "desc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "scrollX": true,
	    "pagingType": "full",
	    "aoColumnDefs":[
	    	{ "bVisible": false, "aTargets": [7,8]}
	    ]

	});
	$("#measurement_table").addClass("table-hover");
}
