import sys
import matplotlib.pyplot as plt

def main():
    # OPEN FILE
    file_path = sys.argv[1]
    file_path2 = sys.argv[2]
    file = open(file_path, "r")
    file_lines = file.readlines()

    file2 = open(file_path2, "r")
    file_lines2 = file2.readlines()

    # ADD ALL CONTENTS TO VIEWING HISTORY OBJECT
    samples = []
    samples2 = []

    for sample in file_lines:
        samples.append(int(sample))

    for sample in file_lines2:
        samples2.append(int(sample))

    plt.xlabel('Time')
    plt.ylabel('Amplitude')
    #plt.axis([0, number, -32000, 20])
    plt.plot(samples, color='red')
    plt.plot(samples2, color='blue')
    plt.axline((0, 0), (len(samples), 0), color='black')

    plt.legend()
    plt.show()

# Standard boilerplate to call the main() function
if __name__ == '__main__':
    main()
