<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
-->
	<xsl:template match="/">
		<h1>Test description</h1>
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="Test">
		<table border='1'>
			<tr bgcolor="yellow" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
			<th colspan="2" align="center">General test info</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test ID</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle"><xsl:value-of select="./@ID"/></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Creation date</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle"><xsl:value-of select="./@date-of-creation"/></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Timeout factor</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle"><xsl:value-of select="./@timeout"/></td>
			</tr>
			<tr bgcolor="yellow" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
				<th colspan="2" align="center">Test description</th>
			</tr>
			<xsl:for-each select="Description">
				<tr>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Description:</td>
					<td>
						<pre><xsl:value-of select="."/></pre>
					</td>
				</tr>
			</xsl:for-each>
			<xsl:for-each select="APITestDescription">
				<tr>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Tested class(es):</td>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="TestedClass">
					  	<b><xsl:value-of select="./@name"/>; </b>
						</xsl:for-each>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Tested method(s):</td>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="TestedMethod">
							<b><xsl:value-of select="./@name"/>; </b>
						</xsl:for-each>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Description:</td>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="Description">
							<pre><xsl:value-of select="."/></pre>
						</xsl:for-each>
					</td>
				</tr>
			</xsl:for-each>
			<xsl:for-each select="MulticaseTestDescription">
				<tr>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Tested class(es):</td>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="TestedClass">
					  	<b><xsl:value-of select="./@name"/>; </b>
						</xsl:for-each>
					</td>
				</tr>
				<xsl:for-each select="Case">
					<tr>
						<th colspan="2" align="center" bgcolor="#FFFFCC" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
							Test case ID: <xsl:value-of select="./@ID"/>
						</th>
					</tr>
					<tr>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Tested method(s)</td>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
							<xsl:for-each select="TestedMethod">
								<xsl:value-of select="./@name"/>;&#32;
							</xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test description</td>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="Description">
							<pre><xsl:value-of select="."/></pre>
						</xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test precondition</td>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="Precondition">
							<pre><xsl:value-of select="."/></pre>
						</xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Expected condition</td>
						<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="Expected">
							<pre><xsl:value-of select="."/></pre>
						</xsl:for-each>
						</td>
					</tr>
				</xsl:for-each>
			</xsl:for-each>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Source</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Link to source(s)</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
					<xsl:for-each select="Source">
							<i><a href="{./@name}"><xsl:value-of select="./@name"/></a>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Runner</th>
			</tr>
			<tr>
				<xsl:for-each select="Runner">
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle"><xsl:value-of select="./@ID"/></td>
					<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
						<xsl:for-each select="Param">
								<p><b>Execute:</b><xsl:value-of select="./@value"/></p>
							<b>with option(s):</b>
							<xsl:for-each select="Option">
								<xsl:value-of select="./@name"/>=<xsl:value-of select="./@value"/>;
							</xsl:for-each>
						</xsl:for-each>
					</td>
				</xsl:for-each>
			</tr>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Resource</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Depends on</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
					<xsl:for-each select="Resource">
						<i><xsl:value-of select="./@name"/>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Restriction</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test's restriction(s)</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
				<xsl:for-each select="Restriction">
						<i><xsl:value-of select="./@name"/>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Keywords</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test marked by keyword(s)</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
					<xsl:for-each select="Keyword">
						<i><xsl:value-of select="./@name"/>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow">
				<th colspan="2" align="center">Author</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Name(s)</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
					<xsl:for-each select="Author">
						<i><xsl:value-of select="./@value"/>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
				<th colspan="2" align="center">Modification</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Test was updated</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">&#32;
					<xsl:for-each select="Modification">
						<i><xsl:value-of select="./@author"/> on <xsl:value-of select="./@date"/>; </i>
					</xsl:for-each>
				</td>
			</tr>
			<tr bgcolor="yellow" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
				<th colspan="2" align="center">Copyright</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">Copyrighted by</td>
				<td bgcolor="#FFFFAA" text="#000000" link="#444444" vlink="#7E7E7E" valign="middle">
					<xsl:for-each select="Copyright">
						<xsl:value-of select="./@value"/><br></br>
					</xsl:for-each>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>