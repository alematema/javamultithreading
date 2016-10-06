# javamultithreading
A swing java desktop application that implements fork/join, using low-level concurrency java api.
It highlights the execution time contrast between this parallel version and a NON-parallel (a sequencial) one.

#Running and resetting the application

	# Running the app
		# MAIN ENTRY POINT : execute the file br.edu.undra.app.EncoderDecoderMvcDriver on your IDE
	# If you wish to fast/slow the task execution time, do as following:
		# fasting ... just decrement the value of the field longTaskDurationSimulationTime at br.edu.undra.model.MessageEncoder
		# slowing ... just increment the value of the field longTaskDurationSimulationTime at br.edu.undra.model.MessageEncoder
	# If you wich to increase/decrease the number of threads, do as following :
		# increasing # threads ... just decrement the value of the field tokensPerTask at br.edu.undra.view.EncoderDecoderPanel
		# decreasing # threads ... just increment the value of the field tokensPerTask at br.edu.undra.view.EncoderDecoderPanel
