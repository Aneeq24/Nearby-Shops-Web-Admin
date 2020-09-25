// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('table.display').DataTable();
} );

$(document).ready(function() {
    $('#dataTableActivity').DataTable({
        "order": [[ 0, 'desc' ]]
    });
});
