function [ H_ransac ] = RANSAC_Wrapper(matches, fittingfn, distfn, ...
    degenfn, s, t, feedback, maxDataTrials, maxTrials)
%RANSAC_Wrapper
    x1 = [matches(:, 1:2), ones(1, size(matches, 1))'];
    x2 = [matches(:, 3:4), ones(1, size(matches, 1))'];
    x1 = x1';
    x2 = x2';

    [H, inliers] = ransac([x1; x2], fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials);
    H_fit = fittingfn([x1(:,inliers)', x2(:,inliers)']);
    H_ransac = H';

end