<?xml version="1.0"?>
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
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
 
   <xsl:output method="html" encoding="iso-8859-1" indent="no"/>

   <xsl:template match="table">
      <table border="1"><xsl:apply-templates/></table>
   </xsl:template>
   
   <xsl:template match="table/team">
            <tr><xsl:apply-templates/></tr>
   </xsl:template>

   <xsl:template match="table/team/name">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/games">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/win">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/winover">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/draw">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/defeatover">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/defeat">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/difference">
      <td><xsl:apply-templates/></td>
   </xsl:template>

   <xsl:template match="table/team/point">
      <td><xsl:apply-templates/></td>
   </xsl:template>

</xsl:stylesheet>

