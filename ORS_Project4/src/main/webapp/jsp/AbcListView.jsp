<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.ABCListCtl"%>
<%@page import="com.rays.pro4.Bean.Abcbean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>ABC List</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/ValidateToInput.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1900:2024',
		
		});
	});
</script>

<script>
	function validateNumericInput(inputField) {
		// Get the value entered by the user
		var inputValue = inputField.value;

		// Regular expression to check if the input is numeric
		var numericPattern = /^\d*$/;

		// Test the input value against the numeric pattern
		if (!numericPattern.test(inputValue)) {
			// If input is not numeric, clear the field
			inputField.value = inputValue.replace(/[^\d]/g, ''); // Remove non-numeric characters
		}
	}
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.Abcbean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.ABC_LIST_CTL%>" method="post">
		<center>

			<div align="center">
				<h1>ABC List</h1>

				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List plist = (List) request.getAttribute("plist");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<Abcbean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr>
					<th></th>

					<!-- 	<td align="center"> -->
					<label>Name:</font>
					</label>
					<input type="text" name="name" align="left"
						placeholder="Enter name" onkeypress="return ValidateInput(event)"
						value="<%=ServletUtility.getParameter("name", request)%>">
					&nbsp; &nbsp;

					<label>Company:</font>
					</label>
					<input type="text" name="company" align="left"
						placeholder="Enter company"
						onkeypress="return ValidateInput(event)"
						value="<%=ServletUtility.getParameter("company", request)%>">
					&nbsp; &nbsp;


					<label>Type</font>:
					</label>
					<%=HTMLUtility.getList("type", String.valueOf(bean.getType()), plist)%>

					&nbsp;
					<label> Date </font>
					</label>
					<input type="text" name="dob" id="udatee" placeholder="Enter Date"
						value="<%=ServletUtility.getParameter("dob", request)%>">
					&nbsp;


						
						<label>Amount:</font> 
					</label> <input type="text" name="amount"
						placeholder="Enter amount " oninput="validateNumericInput(this)" 
						value="<%=ServletUtility.getParameter("amount", request)%>"> &nbsp;
						


					<label>Mobile:</font>
					</label>
					<input type="text" name="mobile" placeholder="Enter mobile "
						oninput="validateNumericInput(this)"  maxlength="10"
						value="<%=ServletUtility.getParameter("mobile", request)%>">
					&nbsp;



					<input type="submit" name="operation"
						value="<%=ABCListCtl.OP_SEARCH%>"> &nbsp;
					<input type="submit" name="operation"
						value="<%=ABCListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: pink">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Name</th>
					<th>Amount</th>
					<th>Date</th>
					<th>Type</th>
					<th>Company</th>
					<th>Mobile</th>

					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>



				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=bean.getName()%></td>
					<td><%=bean.getAmount()%></td>
					<td><%=bean.getDate()%></td>
					<td><%=bean.getType()%></td>
					<td><%=bean.getCompany()%></td>
					<td><%=bean.getMobile()%></td>
					
					<td><a href="ABCCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>

			<table width="100%">


				<tr>
					<th></th>
					<%
						if (pageNo == 1) {
					%>
					<td><input type="submit" name="operation" disabled="disabled"
						value=<%=ABCListCtl.OP_PREVIOUS%>></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value=<%=ABCListCtl.OP_PREVIOUS%>></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value=<%=ABCListCtl.OP_DELETE%>></td>
					<td><input type="submit" name="operation"
						value=<%=ABCListCtl.OP_NEW%>></td>


					<td align="right"><input type="submit" name="operation"
						value=<%=ABCListCtl.OP_NEXT%>
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>

				</tr>

			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value=<%=ABCListCtl.OP_BACK%>></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">
	</form>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>

	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>

