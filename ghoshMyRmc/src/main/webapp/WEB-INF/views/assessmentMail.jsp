<div class="containerPanel" style="margin: 16px; font-size: 15px;">
	<div id="tabs" style="font-family: unset;">
		<ul>
			<li><a href="#tabs-1">Approver</a></li>
			<li><a href="#tabs-2">Assessor</a></li>
		</ul>
		<div id="tabs-1">
			<div>
				<table
					style="background: #FBD6D6; padding: 15px; width: 100%; border-top: 1px solid #AD5C5C;">
					<tbody>
						<tr>
							<td><div>Message</div></td>
							<td colspan="3"><textarea name="message" rows="3"
									id="bodyMsg"
									style="width: 100%; padding: 12px; resize: none; cursor: auto; border: 1px; border-style: solid; border-color: #AD5C5C; background-color: #FFFFFF; overflow: auto; font-size: 12px; font-family: Arial, Helvetica, sans-serif;"></textarea>
							</td>
						</tr>
						<tr>
							<td style="vertical-align: top; padding-top: 12px;"><div>
									Mail<br> Template
								</div></td>
							<td colspan="3" style="vertical-align: top; padding-top: 12px;">
								<div align="left" class="mailBodyTemplate"
									style="background: #FFF; border: 1px solid #AD5C5C; padding: 12px; font-size: 12px; height: 405px; overflow: auto;">
									Dear User,<br>
									<br>Our records indicate that mitigation activity for the
									following Client/Department is pending on MyRMC Tool and
									require immediate attention:<br>
									<br>Name :
									<client department="" name="">
									<br>
									Location : <location name="">
									<br>
									LOB : <lob name="">
									<br>
									Assessor Name : <assessor name="">
									<br>
									Approver Name : <approver name="">
									<br>
									<br>
									You as the registered Approver for this Client/Department to
									conduct Information Security Risk Assessment is requested to
									guide the Assessor to take either of the following actions:<br>
									1) Provide the mitigation date with action plan on tool and
									implement the same. Once implemented, update the response to
									indicate closure.<br>
									2) Accept the risk in case it cannot be mitigated and mention
									the justification in comments section. Upload the mitigation
									evidence as attachment against the accepted risk.<br>
									<br>
									Please use the following link to launch MyRMC Tool:
									www.medintact.com<br>
									<br>
									Below are the risk sources to be mitigated:<br>
									<table width="100%" border="1" cellspacing="0" cellpadding="5"
										style="font-family: Arial; font-size: 13">
										<tbody>
											<tr bgcolor="#DA7373">
												<td align="center" nowrap=""><font color="#FFFFFF">Category</font></td>
												<td align="center" nowrap=""><font color="#FFFFFF">Control</font></td>
												<td align="center" nowrap=""><font color="#FFFFFF">Mitigation
														Date</font></td>
												<td align="center" nowrap=""><font color="#FFFFFF">Status</font></td>
											</tr>
											<tr>
												<td nowrap="" align="center">--</td>
												<td nowrap="" align="center">--</td>
												<td nowrap="" align="center">--</td>
												<td nowrap="" align="center">--</td>
											</tr>
										</tbody>
									</table>
									<br>
									<br>
									You should have received a system generated mail from with
									login credentials. In case of a challenge or you are not able
									to locate the email, please drop a note at querymedin@gmail.com<br>
									<br>
									Regards<br>
									MyRMC Team @ Medin TACT</approver></assessor></lob></location></client>
								</div>
							</td>
						</tr>

					</tbody>
				</table>
			</div>
		</div>
		<div id="tabs-2">
			<p>Morbi tincidunt, dui sit amet facilisis feugiat, odio metus
				gravida ante, ut pharetra massa metus id nunc. Duis scelerisque
				molestie turpis. Sed fringilla, massa eget luctus malesuada, metus
				eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet
				fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam.
				Praesent in eros vestibulum mi adipiscing adipiscing. Morbi
				facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut
				posuere viverra nulla. Aliquam erat volutpat. Pellentesque
				convallis. Maecenas feugiat, tellus pellentesque pretium posuere,
				felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris
				consectetur tortor et purus.</p>
		</div>
	</div>
</div>
<script>
	$(function() {
		$("#tabs").tabs();
	});
</script>