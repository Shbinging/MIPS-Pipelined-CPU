cd $env(SOC_DIR)
open_project $env(SOC_XPR)
generate_target Simulation [get_files $env(SOC_DIR)/../../testbench/noop_sim.bd]
export_ip_user_files -of_objects [get_files $env(SOC_DIR)/../../testbench/noop_sim.bd] -no_script -sync -force -quiet
launch_simulation
run all
exit
