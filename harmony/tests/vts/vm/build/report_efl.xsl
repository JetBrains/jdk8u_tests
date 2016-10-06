<?xml version='1.0'?>
<!-- 

  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at
     
      http://www.apache.org/licenses/LICENSE-2.0
     
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
  implied.  See the License for the specific language governing
  permissions and limitations under the License.

 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<h1>Tests analysing report</h1>
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="property-list">
		<p><a name="Total"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="2" align="center">General properties</th></tr>
			<xsl:for-each select="property-item">
				<tr>
				<td>
					<xsl:if test="@name='date'">
						<strong><xsl:value-of select="./@name"/></strong>
					</xsl:if>
					<xsl:if test="@name!='date'">
						<a href="#{./@name}"><strong><xsl:value-of select="./@name"/></strong></a>
					</xsl:if>
				</td>
				<td><xsl:value-of select="."/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template name="cfg">
		<xsl:param name="x"/>
		<xsl:for-each select="/Report/cfg-list/cfg-item">
			<xsl:if test="@name='test suite result'">
				<xsl:value-of select="."/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="cfg-list">		
		<p><a name="Config"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="2" align="center">Configuration</th></tr>
			<xsl:for-each select="cfg-item">
				<tr>
				<td><strong><xsl:value-of select="./@name"/></strong></td>
				<td><xsl:value-of select="."/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="expected-passed-list">
		<p><a name="Expected passed"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="2" align="center">Expected passed tests</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="unexpected-passed-list">
		<p><a name="Unexpected passed"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Unexpected passed tests</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="expected-failed-list">
		<p><a name="Expected failed"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Expected failed tests</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="unexpected-failed-list">
		<p><a name="Unexpected failed"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Unexpected failed tests</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="expected-error-list">
		<p><a name="Expected error"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests with expected error</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="unexpected-error-list">
		<p><a name="Unexpected error"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests with unexpected error</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="skipped-list">
		<p><a name="Skipped"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests were skipped</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td style="white-space: nowrap"><xsl:value-of select="./test_id"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="expected-unknown-status-list">
		<p><a name="Expected unknown status"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests completed expectedly with exit code which is not recognized by harness as known test execution code</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="unexpected-unknown-status-list">
		<p><a name="Unexpected unknown status"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests completed unexpectedly with exit code which is not recognized by harness as known test execution code</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td>
					<xsl:variable name="root">
						<xsl:call-template name="cfg">
							<xsl:with-param name="x" select="aaa"/>
						</xsl:call-template>
					</xsl:variable>	
					<a href="{./test_id}.thr"><xsl:value-of select="./test_id"/><xsl:value-of select="./case_id"/></a></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
	<xsl:template match="not-run-list">
		<p><a name="Not run"></a><table>
			<tr bgcolor="#CCCCCC">
			<th colspan="5" align="center">Tests were not run recently.</th></tr>
			<tr style="font: bold;">
			<td>#</td>
			<td>TestID</td>
			<td>BugID</td>
			<td align="center">Reason</td>
			<td>Comment</td>
			</tr>
			<xsl:for-each select="list-item">
				<tr>
				<td><b><xsl:number value="position()" format="1" /></b></td>
				<td style="white-space: nowrap"><xsl:value-of select="./test_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./bug_id"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./reason"/></td>
				<td style="white-space: nowrap"><xsl:value-of select="./comment"/></td>
				</tr>
			</xsl:for-each>
		</table></p>
	</xsl:template>
</xsl:stylesheet>
