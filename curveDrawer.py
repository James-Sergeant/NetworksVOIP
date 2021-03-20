import sys
import matplotlib.pyplot as plt

def getDataFromFile(file_path):
    file = open(file_path, "r")
    file_lines = file.readlines()
    samples = []
    for sample in file_lines:
        samples.append(int(sample))
    return samples

def main():

    data = []

    for arg in sys.argv[1:]:
        data.append(getDataFromFile(arg))

    plt.xlabel('Time')
    plt.ylabel('Amplitude')

    for i,graph in enumerate(data):
        plt.plot(graph, label=sys.argv[i+1])

    plt.axhline(y=0, color='black')

    plt.legend()
    plt.show()

# Standard boilerplate to call the main() function
if __name__ == '__main__':
    main()
