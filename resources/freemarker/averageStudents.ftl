<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<settings>
		<animation enabled="True" />
	</settings>
	<charts>
		<chart plot_type="Pie">
			<data_plot_settings enable_3d_mode="false">
				<pie_series>
					<tooltip_settings enabled="true">
						<format>
							{%Name}
							GPA: {%Value}
</format>
					</tooltip_settings>
					<label_settings enabled="true">
						<background enabled="false" />
						<position anchor="Center" valign="Center" halign="Center"
							padding="20" />
						<font color="White">
							<effects>
								<drop_shadow enabled="true" distance="2" opacity="0.5"
									blur_x="2" blur_y="2" />
							</effects>
						</font>
						<format>{%YPercentOfSeries}{numDecimals:1}%</format>
					</label_settings>
				</pie_series>
			</data_plot_settings>
			<data>
				<series name="Series 1" type="Pie">
					<#list chartProperties as cp>
					<point name="${cp[0]}" y="${cp[1]}">
						<attributes>
							<attribute name="fullName">${cp[0]}</attribute>
							<attribute name="methodName">${methodName}</attribute>
						</attributes>
					</point>
					</#list>
				</series>
			</data>
			<chart_settings>
				<title enabled="true" padding="15">
					<text>${chartName}</text>
				</title>
				<legend enabled="true" position="Bottom" align="Spread"
					ignore_auto_item="true" padding="15">
					<format>{%Icon} {%Name} ({%YValue})</format>
					<template>
					</template>
					<title enabled="true">
						<text>${chartName}</text>
					</title>
					<columns_separator enabled="false" />
					<background>
						<inside_margin left="10" right="10" />
					</background>
					<items>
						<item source="Points" />
					</items>
				</legend>
			</chart_settings>
		</chart>
	</charts>
</anychart>