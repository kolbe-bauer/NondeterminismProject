#! /usr/bin/env python3
from pprint import pprint
import sys
import os

'''
To run script: ./find_bugs.py ./bug-outputs/XYSeries.out
Or to run on all contege out files: ./find_bugs.py
0 = deadlock bug, 1 = non-deadlock bug
'''

def process_bug_output(file_name):
    bugs = []
    class_name = file_name.split('/')[-1].split('.')[0]

    # i forgot if python has enums
    # all_states = ('none', 'prefix', 'suffix1' , 'suffix2', 'exception')
    state = 0

    bug_found = False
    prefix = []
    suffix = [[], []]
    exception = []
    bug_type = -1

    with open(file_name, 'r') as f:
        for line in f:
            line = line.strip()
            if len(line) == 0:
                continue

            if 'Found a thread safety violation' in line:
                bug_found = True
            elif 'Starting Java scheduler-based execution' in line:
                bug_found = False
            elif bug_found == False:
                state = 0
                continue

            if 'Sequential prefix' in line:
                state = 1
                continue
            elif 'Concurrent suffixes' in line:
                state = 2
                continue
            elif 'vs.' in line and state == 2:
                state = 3
                continue
            elif 'Exception Found' in line:
                state = 4
                bug_type = 0 if 'TimeoutException' in line else 1 # 0 for deadlock, 1 for data race
                exception.append(line)
                continue

            if state == 1:
                prefix.append(line)
            elif state == 2:
                suffix[0].append(line)
            elif state == 3:
                suffix[1].append(line)
            elif state == 4:
                if class_name in line:
                    exception.append(line)
                elif 'at' in line:
                    continue
                else:
                    state = 0
                    bug_found = False
                    bugs.append({
                        'prefix': prefix, 
                        'suffix': suffix, 
                        'exception': exception, 
                        'bug_type': bug_type
                    })
                    prefix = []
                    suffix = [[], []]
                    exception = []
                    bug_type = -1

    return bugs


def process_jlint_logs(class_name, ret):
    bug_cause = []

    error_classes = set()
    for e_line in ret[0]['exception']:
        if 'at' in e_line: 
            error_path = e_line.split()[1].split('.')[0:-1]
            error_class = '/'.join(error_path)
            error_classes.add(error_class)
    
    with open(f'logs_jlint/{class_name}.log_filtered', 'r') as f:
        for line in f:
            warning_path = line.split('.')[1].split('/')[2::]
            warning_class = '/'.join(warning_path)

            if warning_class in error_classes:
                bug_cause.append(line)


    return bug_cause


if __name__ == '__main__':
    
    # if argv[1] is defined run for specific class
    if len(sys.argv) > 1:
        output_files = [sys.argv[1]]
    else:
        output_files = [ f'bug-outputs/{f}' for f in os.listdir('bug-outputs')]

    # if no class defined, run on all in bug-output
    for file_name in output_files:
        class_name = file_name.split('/')[-1].split('.')[0]
        print('\n' + class_name)

        ret = process_bug_output(file_name)

        if not ret:
            print('No bugs found in ConTeGe report')
            continue
        else:
            # print(ret)
            pprint(ret)

        static_bug = process_jlint_logs(class_name, ret)
        
        if not static_bug:
            print('No corresponding bugs found in jlint logs')
        else:
            # print(static_bug)
            pprint(static_bug)



