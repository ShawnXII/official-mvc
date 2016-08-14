var TableEditable = function () {

    return {
        //main function to initiate the module
        init: function (msg,url,columns) {
            var oTable = $('#tableData').dataTable({
            	"searching":true,
                "aLengthMenu": [
                    [10, 20, 50, -1],
                    [10, 20, 50, 100] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 20,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "每页 _MENU_ 条",
                    "oPaginate": {
                        "sPrevious": "上一页",
                        "sNext": "下一页",
                        "sFirst" : "首页",
                        "sLast" : "未页"
                    },
                    "sProcessing":"正在加载数据...",
                    "sEmptyTable" : "没有找到记录",
                    "sInfo" : "从_START_ 到 _END_ 条记录-总记录数为 _TOTAL_ 条",
                    "sInfoEmpty" : "记录数为0",
                    "sInfoFiltered" : "(全部记录数 _MAX_  条)",
                    "sInfoEmpty":"",
                    "sZeroRecords" : "没有找到记录"
                },
                "bJQueryUI": true,
                "bPaginate" : true,//分页按钮
                "bFilter" : true,//搜索栏
                "bLengthChange" : true,//每行显示的记录数
                "iDisplayLength" : 10,// 每页显示行数
                "bSort" : false,// 排序
                "bInfo" : true,
               "bWidth":true,
                "bScrollCollapse": true,
                //隐藏搜索框
               // "sPaginationType" : "full_numbers",// 分页，一共两种样式 另一种为two_button  是datatables默认  
            /*    "bDestroy": true,
               "bSortCellsTop": true,*/
      /*         "sServerMethod": "POST",
               "sAjaxSource": "classifyData.htm",*/
            /*   "aoColumns": [
                            { "sName": "ChePaiHao"},
                            { "sName": "ChePaiYanSe" },
                            { "sName": "ChePaiYanSe" },
                            { "sName": "ChePaiYanSe" },
                            { "sName": "ChePaiYanSe" },
                            { "sName": "ChePaiYanSe" },
                            { "sName": "ChePaiYanSe" }
                         ],*/
              
            });      
        }
    };
}();