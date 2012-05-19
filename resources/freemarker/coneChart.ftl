<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<settings>
		<animation enabled="True" />
	</settings>
	<charts>
		<chart plot_type="CategorizedHorizontal">
			<data_plot_settings default_series_type="Bar">
				<bar_series group_padding="0.3" shape_type="Cone">
					<tooltip_settings enabled="True">
						<background>
							<border color="DarkColor(%Color)" />
						</background>
						<format>
							{%Name}
							Количество: {%YValue}{numDecimals:0}
						</format>
					</tooltip_settings>
					<bar_style>
						<effects>
							<bevel enabled="false" />
						</effects>
					</bar_style>
				</bar_series>
			</data_plot_settings>
			<axes>
				<y_axis position="Opposite">
					<title>
						<text>hola</text>
					</title>
				</y_axis>
			</axes>
			<chart_settings>
				<title enabled="true">
					<text>${chartName}</text>
				</title>
				<axes>
					<y_axis position="Opposite">
						<title>
							<text>${yAxis}</text>
						</title>
					</y_axis>
					<x_axis>
						<title>
							<text>${xAxis}</text>
						</title>
					</x_axis>
				</axes>
			</chart_settings>
			<data>
				<series name="Series 1">
					<#list chartProperties as cp>
					<point name="${cp[0]}" y="${cp[1]}" hatch_type="${hatchType}" >
						<attributes>
							<attribute name="fullName">${cp[0]}</attribute>
							<attribute name="methodName">${methodName}</attribute>
						</attributes>
					</point>
					</#list>
				</series>
			</data>
		</chart>
	</charts>
</anychart>