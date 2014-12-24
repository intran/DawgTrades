<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script>
$(function () {

    $("tr").dblclick(function () {
        $( "#report" ).val($(this).children().eq(1).text());
        $( "#id" ).val($(this).children().eq(0).text());
        if ($(this).children().eq(2).text()== "1"){
            $("#1").prop("checked", true)
        }
        if ($(this).children().eq(2).text()== "2"){
            $("#2").prop("checked", true)
        }
        if ($(this).children().eq(2).text()== "3"){
            $("#3").prop("checked", true)
        }           
    });
});
</script>

<#import "default.ftl" as default>
<@default.mainLayout "Settings">
<h1>Experience Report</h1>
<#if error??>
        <div class="col-md-12 alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only"> Error: </span>
                ${error}
        </div>
</#if>

<div class="row">
    <form id="f1" role="form" action="experience" method="post">
    <div class="form-group">
        <label for="listId"> Registered Users </label>
        <select id = "listId" name = "listId" class="form-control">
        <#if users??>   
            
            <#list users as user>  
                <#if userselct?? && userselct == user.getId()?string>          
                    <option selected value= ${user.getId()} >${user.getFirstName()}  ${user.getLastName()}</option>
                <#else>
                    <option value=${user.getId()}>${user.getFirstName()}  ${user.getLastName()}</option>
                </#if>
            </#list>
            </select></br>
        </#if>

        <div class="form-group">
            <label for="report"> Report </label>
            <input type="text" id = "report" name="report" class="form-control" >
            <input type="hidden" id = "id" name="id" >
	</div>
        <div class="form-group">
            <label for="report"> Rate </label>
            <label class="radio-inline">
                <input type="radio" name="rate" id="1" value="1"> 1
            </label>
            <label class="radio-inline">
                <input type="radio" name="rate" id="2" value="2"> 2
            </label>
            <label class="radio-inline">
                <input type="radio" name="rate"  id="3" value="3"> 3
            </label>
        </div>
        <button id= "add" name="add" type="submit" > Add </button>
        <button id= "update" name="update" type="submit" > Update </button>
        <button id= "delete" name="delete" type="submit" > Delete </button>
        <button id= "show" name="show" type="submit" > Show Reports </button>
        <button id= "reset" name="reset" type="reset" > Reset </button>

    </div>

    <div class="form-group">
    <h4>Report History</h4>
      <table class="table table-striped">

        <tr>
            <th>Report</th> <th>Report</th>  <th>Rate</th> <th>Date</th> <th>Reviewer</th>
        </tr>
        <#if reports??>

            <#list reports as report>
                <#if loggedInUser.getId() == report.getReviewer().getId()>       
                    <tr contenteditable='true'>
                <#else>
                    <tr>
                </#if>
                    <td >${report.getId()}</td> <td >${report.getReport()}</td>
                    <td >${report.getRating()}</td> <td >${report.getDate()}</td>
                    <td >${report.getReviewer().getFirstName()}</td>
                </tr>
            </#list>
        </#if>
      </table>
    </div>
    </form>
</div>

</div>

</@default.mainLayout>
