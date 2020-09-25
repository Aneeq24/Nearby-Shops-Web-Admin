// Call the dataTables jQuery plugin
$(document).ready(function() {
   /* $('#dataTable').DataTable();
    $('#dataTable1').DataTable();*/
 $('table.display').DataTable();
$('#dataTable').DataTable();
});

$(document).ready(function() {
    $('#category').DataTable({
        "order": [[ 0, 'desc' ]]
    });
});


