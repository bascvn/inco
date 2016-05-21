<!Doctype html>
<html>
<head>
<title>Malevia Service API</title>
<script src="js/jquery.js"></script>  
<script src="http://crypto-js.googlecode.com/svn/tags/3.0.2/build/rollups/md5.js"></script>
    
<style>
 #wrapper {
  margin-left: 0px;
}
#content {
  float: right;
  width: 100%;
  background-color:  #F7F0F7;
}

#cleared {
  clear: both;
}

.textwrapper
{
    border:1px solid #999999;
    margin:5px 0;
    padding:3px;
}
</style>

    
</head>
<body>

<script type="text/javascript">


 

  function submit_form(a_id)
  {
	$("#frm_" + a_id).attr('action', $("#" + a_id).text());
	$("#frm_" + a_id).append($('#AccessToken'));
	$("#frm_" + a_id).submit();
  }
  

</script>

<h1>Malevia Web Service API</h1>

<div id="wrapper">
  <div id="content">

        <h3>General</h3>
		AccessToken: <input type="text" value="" name="AccessToken" id="AccessToken">
		

	 <h3>I. Customer</h3>
	<ul>
		<li>1. check_login</li>
            <form id="frm_customer_check_login" method="POST">
            <ul>
                    <li>URL: <a id="customer_check_login" href="#"  onclick="return submit_form('customer_check_login');">http://<?php echo $_SERVER['HTTP_HOST']?>/sample_api/gateway.php?controller=customer.check_login</a></li>
                    <li>POST Params: </li>
                    UserEmail: <input type="text" value="test@gmail.com" name="CustomerEmail" id="CustomerEmail"><br/>
                    UserPassword: <input type="text" value="1234567" name="CustomerPassword" id="CustomerPassword"><br/>
					PushDeviceTokenValue: <input type="text" value="ce98db1b9150414d9c2f2fcdd5e8686c68fb" name="PushDeviceTokenValue" id="PushDeviceTokenValue"><br/>
				

            </ul>
            </form>

			<li>2. forgot_password</li>
            <form id="frm_customer_forgot_password" method="POST">
            <ul>
                    <li>URL: <a id="customer_forgot_password" href="#"  onclick="return submit_form('customer_forgot_password');">http://<?php echo $_SERVER['HTTP_HOST']?>/sample_api/gateway.php?controller=customer.forgot_password</a></li>
                    <li>POST Params: </li>
                    UserEmail: <input type="text" value="nvlong@gmail.com" name="CustomerEmail" id="CustomerEmail"><br/>
            </ul>
            </form>
	</ul>
	
  </div>
  <div id="cleared"></div>
</div>

</body>
</html>